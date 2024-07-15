import React, { useState, useEffect, useRef, useCallback } from 'react';
import axiosInstance from '../../api/axiosInstance'; // 경로 수정
import { useParams, useNavigate, Link } from 'react-router-dom';
import { DragDropContext, Droppable, Draggable } from 'react-beautiful-dnd';
import { useInView } from 'react-intersection-observer';
import CardFormModal from './CardFormModal';
import SectionEditModal from './SectionEditModal';
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
    const [page, setPage] = useState(0); // 섹션 페이지 상태 추가
    const [hasMoreSections, setHasMoreSections] = useState(true);

    const { ref: observerRef, inView } = useInView({
        threshold: 0.1
    });
    const [showCardModal, setShowCardModal] = useState(false);
    const [showEditModal, setShowEditModal] = useState(false);
    const [editingSection, setEditingSection] = useState(null);

    useEffect(() => {
        fetchBoardDetail();
    }, [boardId]);

    useEffect(() => {
        if (hasMoreSections && inView) {
            fetchSections();
        }
    }, [inView]);

    async function fetchBoardDetail() {
        try {
            const token = localStorage.getItem('Authorization');
            const response = await axiosInstance.get(`/api/boards/${boardId}`, {
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
            const response = await axiosInstance.get(`/api/sections?boardId=${boardId}&page=${page}`, {
                headers: { Authorization: token }
            });
            const fetchedSections = response.data.data.content;

            const sectionsWithCards = await Promise.all(fetchedSections.map(async (section) => {
                const cardsResponse = await axiosInstance.get(`/api/cards?boardId=${boardId}&sectionId=${section.sectionId}&page=0`, {
                    headers: { Authorization: token }
                });
                section.cards = cardsResponse.data.data.content;
                return section;
            }));

            setSections(prevSections => [...prevSections, ...sectionsWithCards]);
            setPage(prevPage => prevPage + 1);
            setHasMoreSections(response.data.data.content.length > 0);
        } catch (error) {
            console.error('섹션 및 카드 목록 가져오기 실패:', error);
        }
    }

    async function fetchMoreCards(sectionId, cardPage) {
        try {
            const token = localStorage.getItem('Authorization');
            const response = await axiosInstance.get(`/api/cards?boardId=${boardId}&sectionId=${sectionId}&page=${cardPage}`, {
                headers: { Authorization: token }
            });

            const newCards = response.data.data.content;

            setSections(prevSections =>
                prevSections.map(section =>
                    section.sectionId === sectionId
                        ? { ...section, cards: [...section.cards, ...newCards], cardPage: cardPage + 1, hasMoreCards: newCards.length > 0 }
                        : section
                )
            );
        } catch (error) {
            console.error('추가 카드 목록 가져오기 실패:', error);
        }
    }

    async function handleAddSession() {
        try {
            const token = localStorage.getItem('Authorization');
            await axiosInstance.post(`/api/sections`, {
                contents: contents,
                boardId: boardId
            }, {
                headers: { Authorization: token }
            });
            setPage(0);
            setSections([]);
            fetchSections();
            setContents('');
        } catch (error) {
            console.error('섹션 추가 실패:', error);
        }
    }

    async function handleAddCard(title, contents, dueDate) {
        try {
            const token = localStorage.getItem('Authorization');
            await axiosInstance.post(`/api/cards`, {
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

    async function handleUpdateSection(sectionId, newContents) {
        try {
            const token = localStorage.getItem('Authorization');
            await axiosInstance.put(`/api/sections/${sectionId}`, {
                contents: newContents
            }, {
                headers: { Authorization: token }
            });
            fetchSections();
            setShowEditModal(false);
        } catch (error) {
            console.error('섹션 수정 실패:', error);
        }
    }

    function openCardModal(sectionId) {
        setSelectedSectionId(sectionId);
        setShowCardModal(true);
    }

    function closeCardModal() {
        setShowCardModal(false);
    }

    function openEditModal(section) {
        setEditingSection(section);
        setShowEditModal(true);
    }

    function closeEditModal() {
        setShowEditModal(false);
    }

    async function handleDeleteSession(sectionId) {
        const confirmDelete = window.confirm("정말로 이 섹션을 삭제하시겠습니까?");
        if (confirmDelete) {
            try {
                const token = localStorage.getItem('Authorization');
                await axiosInstance.delete(`/api/sections/${sectionId}`, {
                    headers: { Authorization: token }
                });
                setPage(0);
                setSections([]);
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
                await axiosInstance.delete(`/api/cards/${cardId}`, {
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

    async function updateSectionPosition(sectionId, newPosition) {
        try {
            const token = localStorage.getItem('Authorization');
            await axiosInstance.put(`/api/sections/move`, {
                sectionId: sectionId,
                newPosition: newPosition
            }, {
                headers: { Authorization: token }
            });
            fetchSections();
        } catch (error) {
            console.error('섹션 순서 업데이트 실패:', error);
        }
    }

    async function updateCardPosition(cardId, newPosition, sectionId) {
        try {
            const token = localStorage.getItem('Authorization');
            await axiosInstance.put(`/api/cards/move`, {
                cardId: cardId,
                newPosition: newPosition,
                sectionId: sectionId
            }, {
                headers: { Authorization: token }
            });
            fetchSections();
        } catch (error) {
            console.error('카드 순서 업데이트 실패:', error.response.data.message);
        }
    }

    async function handleOnDragEnd(result) {
        const { destination, source, draggableId, type } = result;

        if (!destination) return;

        if (destination.droppableId === source.droppableId && destination.index === source.index) {
            return; // 위치가 변경되지 않은 경우 아무 작업도 하지 않음
        }

        if (type === 'SECTION') {
            const newSections = Array.from(sections);
            const [movedSection] = newSections.splice(source.index, 1);
            newSections.splice(destination.index, 0, movedSection);

            setSections(newSections); // 상태 업데이트

            await updateSectionPosition(movedSection.sectionId, destination.index);
        } else if (type === 'CARD') {
            const startSectionIndex = sections.findIndex(section => section.sectionId.toString() === source.droppableId);
            const endSectionIndex = sections.findIndex(section => section.sectionId.toString() === destination.droppableId);

            if (startSectionIndex === -1 || endSectionIndex === -1) return;

            const startSection = sections[startSectionIndex];
            const endSection = sections[endSectionIndex];

            const newStartCards = Array.from(startSection.cards);
            const [movedCard] = newStartCards.splice(source.index, 1);

            if (startSection === endSection) {
                newStartCards.splice(destination.index, 0, movedCard);
                const newSections = Array.from(sections);
                newSections[startSectionIndex].cards = newStartCards;

                setSections(newSections);
                await updateCardPosition(movedCard.cardId, destination.index, startSection.sectionId);
            } else {
                const newEndCards = Array.from(endSection.cards);
                newEndCards.splice(destination.index, 0, movedCard);

                const newSections = Array.from(sections);
                newSections[startSectionIndex].cards = newStartCards;
                newSections[endSectionIndex].cards = newEndCards;

                setSections(newSections);
                await updateCardPosition(movedCard.cardId, destination.index, endSection.sectionId);
            }
        }
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
                        <Droppable droppableId="droppable-sections" type="SECTION">
                            {(provided, snapshot) => (
                                <div
                                    {...provided.droppableProps}
                                    ref={provided.innerRef}
                                    className={`sections-container ${snapshot.isDraggingOver ? 'dragging-over' : ''}`}
                                >
                                    {sections.length > 0 ? sections.map((section, index) => (
                                        <Draggable key={section.sectionId.toString()} draggableId={section.sectionId.toString()} index={index}>
                                            {(provided, snapshot) => (
                                                <div
                                                    ref={provided.innerRef}
                                                    {...provided.draggableProps}
                                                    {...provided.dragHandleProps}
                                                    className={`section-card ${snapshot.isDragging ? 'dragging' : ''}`}
                                                >
                                                    <div>
                                                        <h4>{section.contents}</h4>
                                                        <p>작성자: {section.nickname}</p>
                                                        <p>작성일: {section.createdAt}</p>
                                                        <p>수정일: {section.modifiedAt}</p>
                                                        <button onClick={() => handleDeleteSession(section.sectionId)} className="button">섹션 삭제</button>
                                                        <button onClick={() => openEditModal(section)} className="button">섹션 수정</button>
                                                        <button onClick={() => openCardModal(section.sectionId)} className="button">카드 추가</button>
                                                        <Droppable droppableId={section.sectionId.toString()} type="CARD">
                                                            {(provided, snapshot) => (
                                                                <div
                                                                    {...provided.droppableProps}
                                                                    ref={provided.innerRef}
                                                                    className={`cards-container ${snapshot.isDraggingOver ? 'dragging-over' : ''}`}
                                                                >
                                                                    {section.cards && section.cards.map((card, index) => (
                                                                        <Draggable key={card.cardId.toString()} draggableId={card.cardId.toString()} index={index}>
                                                                            {(provided, snapshot) => (
                                                                                <div
                                                                                    ref={provided.innerRef}
                                                                                    {...provided.draggableProps}
                                                                                    {...provided.dragHandleProps}
                                                                                    className={`card-item ${snapshot.isDragging ? 'dragging' : ''}`}
                                                                                >
                                                                                    <h5 onClick={() => navigateToCardDetail(card.cardId)}
                                                                                        className="card-title">{card.title}</h5>
                                                                                    <p>{card.contents}</p>
                                                                                    <p>작성자: {card.nickname}</p>
                                                                                    <p>작성일: {card.createdAt}</p>
                                                                                    <p>수정일: {card.modifiedAt}</p>
                                                                                    <button onClick={() => handleDeleteCard(card.cardId)}
                                                                                            className="button">카드 삭제
                                                                                    </button>
                                                                                </div>
                                                                            )}
                                                                        </Draggable>
                                                                    ))}
                                                                    {provided.placeholder}
                                                                    <div ref={observerRef} />
                                                                </div>
                                                            )}
                                                        </Droppable>
                                                    </div>
                                                    {showCardModal && selectedSectionId === section.sectionId && (
                                                        <CardFormModal
                                                            onSubmit={handleAddCard}
                                                            onClose={closeCardModal}
                                                        />
                                                    )}
                                                    {showEditModal && editingSection && editingSection.sectionId === section.sectionId && (
                                                        <SectionEditModal
                                                            section={editingSection}
                                                            onSubmit={handleUpdateSection}
                                                            onClose={closeEditModal}
                                                        />
                                                    )}
                                                </div>
                                            )}
                                        </Draggable>
                                    )) : (
                                        <p>섹션이 없습니다. 새 섹션을 추가해 주세요.</p>
                                    )}
                                    {provided.placeholder}
                                    <div ref={observerRef} />
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
