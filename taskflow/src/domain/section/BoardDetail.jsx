import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { useParams, useNavigate, Link } from 'react-router-dom';
import { DragDropContext, Droppable, Draggable } from 'react-beautiful-dnd';
import CardFormModal from './CardFormModal';
import './BoardDetail.css';

function BoardDetail({ onLogout }) {
    const { boardId } = useParams();
    const navigate = useNavigate();
    const [board, setBoard] = useState(null);
    const [sections, setSections] = useState([]);
    const [loading, setLoading] = useState(true);
    const [contents, setContents] = useState('');
    const [selectedSectionId, setSelectedSectionId] = useState(null);
    const [showModal, setShowModal] = useState(false);

    useEffect(() => {
        fetchBoardDetail();
        fetchSections();
    }, [boardId]);

    async function fetchBoardDetail() {
        try {
            const token = localStorage.getItem('Authorization');
            const response = await axios.get(`http://localhost:8080/api/boards/${boardId}`, {
                headers: { Authorization: token }
            });
            setBoard(response.data.data);
        } catch (error) {
            console.error('보드 상세 정보 가져오기 실패:', error);
        } finally {
            setLoading(false);
        }
    }

    async function fetchSections() {
        try {
            const token = localStorage.getItem('Authorization');
            const response = await axios.get(`http://localhost:8080/api/sections?boardId=${boardId}&page=0`, {
                headers: { Authorization: token }
            });
            const sectionsWithCards = await Promise.all(response.data.data.content.map(async (section) => {
                const cardsResponse = await axios.get(`http://localhost:8080/api/cards?boardId=${boardId}&sectionId=${section.sectionId}&page=0`, {
                    headers: { Authorization: token }
                });
                section.cards = cardsResponse.data.data.content;
                return section;
            }));
            setSections(sectionsWithCards);
        } catch (error) {
            console.error('섹션 및 카드 목록 가져오기 실패:', error);
        }
    }

    async function handleAddSession() {
        try {
            const token = localStorage.getItem('Authorization');
            await axios.post(`http://localhost:8080/api/sections`, {
                contents: contents,
                boardId: boardId
            }, {
                headers: { Authorization: token }
            });
            fetchSections();
            setContents('');
        } catch (error) {
            console.error('섹션 추가 실패:', error);
        }
    }

    async function handleAddCard(title, contents, dueDate) {
        try {
            const token = localStorage.getItem('Authorization');
            await axios.post(`http://localhost:8080/api/cards`, {
                title: title,
                contents: contents,
                dueDate: dueDate,
                boardId: boardId,
                sectionId: selectedSectionId
            }, {
                headers: { Authorization: token }
            });
            fetchSections();
        } catch (error) {
            console.error('카드 추가 실패:', error);
        }
    }

    function openModal(sectionId) {
        setSelectedSectionId(sectionId);
        setShowModal(true);
    }

    function closeModal() {
        setShowModal(false);
    }

    async function handleDeleteSession(sectionId) {
        const confirmDelete = window.confirm("정말로 이 섹션을 삭제하시겠습니까?");
        if (confirmDelete) {
            try {
                const token = localStorage.getItem('Authorization');
                await axios.delete(`http://localhost:8080/api/sections/${sectionId}`, {
                    headers: { Authorization: token }
                });
                fetchSections();
            } catch (error) {
                console.error('섹션 삭제 실패:', error);
            }
        }
    }

    async function handleDeleteCard(cardId) {
        const confirmDelete = window.confirm("정말로 이 카드를 삭제하시겠습니까?");
        if (confirmDelete) {
            try {
                const token = localStorage.getItem('Authorization');
                await axios.delete(`http://localhost:8080/api/cards/${cardId}`, {
                    headers: { Authorization: token }
                });
                fetchSections();
            } catch (error) {
                console.error('카드 삭제 실패:', error);
                if (error.response) {
                    console.error('응답 데이터:', error.response.data);
                }
            }
        }
    }

    function handleLogout() {
        localStorage.removeItem('board');
        onLogout();
    }

    function handboradlist() {
        localStorage.removeItem('board');
        navigate('/board'); // 'board' 페이지로 이동
    }

    function navigateToCardDetail(cardId) {
        navigate(`/cards/${cardId}`);
    }

    function handleOnDragEnd(result) {
        const { destination, source } = result;

        if (!destination) return;

        if (destination.droppableId === source.droppableId && destination.index === source.index) {
            return; // 위치가 변경되지 않은 경우 아무 작업도 하지 않음
        }

        const newSections = Array.from(sections);
        const [removed] = newSections.splice(source.index, 1);
        newSections.splice(destination.index, 0, removed);

        setSections(newSections); // 상태 업데이트
    }


    return (
        <div className="board-container">
            <h2 className="board-title">보드 상세 페이지</h2>
            {loading ? (
                <p>로딩 중...</p>
            ) : (
                <div>
                    {board ? (
                        <div>
                            <h3 className="board-content">보드 제목: {board.name}</h3>
                            <p className="board-content">보드 내용: {board.description}</p>
                            <p className="board-info">작성일: {board.createdAt}</p>
                            <p className="board-info">수정일: {board.modifiedAt}</p>
                            <div className="board-title-button">
                                <input
                                    type="text"
                                    value={contents}
                                    onChange={(e) => setContents(e.target.value)}
                                    placeholder="섹션 내용 입력"
                                    className="section-input"
                                />
                                <button onClick={handleAddSession} className="button">섹션 추가</button>
                                <Link to="/">
                                    <button onClick={handleLogout}>로그아웃</button>
                                </Link>
                                <Link to="/board">
                                    <button onClick={handboradlist}>보드 리스트</button>
                                </Link>
                            </div>
                        </div>
                    ) : (
                        <div>
                            <p>해당 보드 정보를 찾을 수 없습니다.</p>
                            <Link to="/board">
                                <button onClick={handboradlist}>보드 리스트</button>
                            </Link>
                        </div>
                    )}
                    <DragDropContext onDragEnd={handleOnDragEnd}>
                        <Droppable droppableId="droppable-sections">
                            {(provided) => (
                                <div {...provided.droppableProps} ref={provided.innerRef} className="sections-container">
                                    {sections.map((section, index) => (
                                        <Draggable key={section.sectionId} draggableId={section.sectionId.toString()} index={index}>
                                            {(provided) => (
                                                <div
                                                    ref={provided.innerRef}
                                                    {...provided.draggableProps}
                                                    {...provided.dragHandleProps}
                                                    className="section-card"
                                                >
                                                    <div>
                                                        <h4>{section.contents}</h4>
                                                        <p>작성자: {section.nickname}</p>
                                                        <p>작성일: {section.createdAt}</p>
                                                        <p>수정일: {section.modifiedAt}</p>
                                                        <button onClick={() => handleDeleteSession(section.sectionId)} className="button">섹션 삭제</button>
                                                        <button onClick={() => openModal(section.sectionId)} className="button">카드 추가</button>
                                                        {section.cards && section.cards.map((card) => (
                                                            <div key={card.cardId} className="card-item">
                                                                <h5 onClick={() => navigateToCardDetail(card.cardId)} className="card-title">{card.title}</h5>
                                                                <p>{card.contents}</p>
                                                                <p>작성자: {card.nickname}</p>
                                                                <p>작성일: {card.createdAt}</p>
                                                                <p>수정일: {card.modifiedAt}</p>
                                                                <button onClick={() => handleDeleteCard(card.cardId)} className="button">카드 삭제</button>
                                                            </div>
                                                        ))}
                                                    </div>
                                                    {showModal && selectedSectionId === section.sectionId && (
                                                        <CardFormModal
                                                            onSubmit={handleAddCard}
                                                            onClose={closeModal}
                                                        />
                                                    )}
                                                </div>
                                            )}
                                        </Draggable>
                                    ))}
                                    {provided.placeholder}
                                </div>
                            )}
                        </Droppable>
                    </DragDropContext>
                </div>
            )}
        </div>
    );
}

export default BoardDetail;
