import React, { useState, useEffect } from 'react';
import axios from 'axios';
import {useParams, useNavigate, Link} from 'react-router-dom';
import CardFormModal from './CardFormModal'; // CardFormModal 컴포넌트 import
import './BoardDetail.css'; // CSS 파일 import

function BoardDetail({ onLogout }) {
    const { boardId } = useParams(); // URL 파라미터에서 boardId 추출
    const navigate = useNavigate(); // useNavigate 훅 사용
    const [board, setBoard] = useState(null); // 보드 상세 정보 상태
    const [sections, setSections] = useState([]); // 섹션 목록 상태
    const [loading, setLoading] = useState(true); // 데이터 로딩 상태
    const [contents, setContents] = useState(''); // 세션 내용 상태
    const [selectedSectionId, setSelectedSectionId] = useState(null); // 선택된 섹션 ID
    const [showModal, setShowModal] = useState(false); // 모달 창 보이기/감추기 상태

    // 보드 상세 정보 가져오기
    const fetchBoardDetail = async () => {
        try {
            const token = localStorage.getItem('Authorization');
            const response = await axios.get(`http://localhost:8080/api/boards/${boardId}`, {
                headers: { Authorization: token }
            });
            console.log('보드 상세 정보 가져오기 성공:', response.data);
            setBoard(response.data.data); // 가져온 보드 상세 정보를 상태에 저장
        } catch (error) {
            console.error('보드 상세 정보 가져오기 실패:', error);
        } finally {
            setLoading(false); // 로딩 상태 변경
        }
    };

    // 섹션 및 카드 목록 가져오기
    const fetchSections = async () => {
        try {
            const token = localStorage.getItem('Authorization');
            const response = await axios.get(`http://localhost:8080/api/sections?boardId=${boardId}&page=0`, {
                headers: { Authorization: token }
            });
            console.log('섹션 목록 가져오기 성공:', response.data);
            const sectionsWithCards = await Promise.all(response.data.data.content.map(async (section) => {
                const cardsResponse = await axios.get(`http://localhost:8080/api/cards?boardId=${boardId}&sectionId=${section.sectionId}&page=0`, {
                    headers: { Authorization: token }
                });
                section.cards = cardsResponse.data.data.content; // 각 섹션에 카드 목록 추가
                return section;
            }));
            setSections(sectionsWithCards); // 가져온 섹션 목록을 상태에 저장
        } catch (error) {
            console.error('섹션 및 카드 목록 가져오기 실패:', error);
        }
    };

    // 컴포넌트가 마운트될 때 보드 정보와 섹션 목록을 가져옴
    useEffect(() => {
        fetchBoardDetail();
        fetchSections();
    }, [boardId]); // boardId가 변경될 때마다 실행

    // 세션 추가 함수
    const handleAddSession = async () => {
        try {
            const token = localStorage.getItem('Authorization');
            const response = await axios.post(`http://localhost:8080/api/sections`, {
                contents: contents,
                userId: 1, // 예시로 고정 값 사용, 실제로는 로그인한 사용자의 ID를 사용해야 합니다.
                boardId: boardId
            }, {
                headers: { Authorization: token }
            });
            console.log('섹션 추가 성공:', response.data);
            // 세션 추가 후 보드 상세 정보를 다시 불러와서 갱신
            fetchSections(); // 섹션 목록 갱신
            setContents(''); // 입력된 세션 내용 초기화
        } catch (error) {
            console.error('섹션 추가 실패:', error);
        }
    };

    // 카드 추가 함수
    const handleAddCard = async (title, contents, dueDate) => {
        try {
            const token = localStorage.getItem('Authorization');
            const response = await axios.post(`http://localhost:8080/api/cards`, {
                title: title,
                contents: contents,
                dueDate: dueDate,
                userId: 1, // 예시로 고정 값 사용, 실제로는 로그인한 사용자의 ID를 사용해야 합니다.
                boardId: boardId,
                sectionId: selectedSectionId
            }, {
                headers: { Authorization: token }
            });
            console.log('카드 추가 성공:', response.data);
            // 카드 추가 후 섹션 목록을 다시 불러와서 갱신
            fetchSections();
        } catch (error) {
            console.error('카드 추가 실패:', error);
        }
    };

    // 모달 창 열기
    const openModal = (sectionId) => {
        setSelectedSectionId(sectionId);
        setShowModal(true);
    };

    // 모달 창 닫기
    const closeModal = () => {
        setShowModal(false);
    };

    // 섹션 삭제 함수
    const handleDeleteSession = async (sectionId) => {
        const confirmDelete = window.confirm("정말로 이 섹션을 삭제하시겠습니까?");
        if (confirmDelete) {
            try {
                const token = localStorage.getItem('Authorization');
                await axios.delete(`http://localhost:8080/api/sections/${sectionId}`, {
                    headers: { Authorization: token }
                });
                console.log('섹션 삭제 성공');
                // 섹션 삭제 후 섹션 목록을 다시 불러와서 갱신
                fetchSections();
            } catch (error) {
                console.error('섹션 삭제 실패:', error);
            }
        }
    };

    // 카드 삭제 함수
    const handleDeleteCard = async (cardId) => {
        const confirmDelete = window.confirm("정말로 이 카드를 삭제하시겠습니까?");
        if (confirmDelete) {
            try {
                const token = localStorage.getItem('Authorization');
                await axios.delete(`http://localhost:8080/api/cards/${cardId}`, {
                    headers: {
                        Authorization: token // 토큰을 헤더에 포함해서 보냄
                    }
                });
                console.log('카드 삭제 성공');
                // 카드 삭제 후 섹션 목록을 다시 불러와서 갱신
                fetchSections();
            } catch (error) {
                console.error('카드 삭제 실패:', error);
                // 오류 메시지를 콘솔에 출력하여 디버깅
                if (error.response) {
                    console.error('응답 데이터:', error.response.data);
                }
            }
        }
    };
    const handleLogout = () => {
        localStorage.removeItem('board');
        onLogout();
    };

    const handboradlist = () => {
        localStorage.removeItem('board');
    };


    // 카드 상세 페이지로 이동하는 함수
    const navigateToCardDetail = (cardId) => {
        navigate(`/cards/${cardId}`);
    };

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
                                    <button onClick={handboradlist}>보더 리스트</button>
                                </Link>
                            </div>

                        </div>
                    ) : (
                        <div>
                            <p>해당 보드 정보를 찾을 수 없습니다.</p>
                            <Link to="/board">
                                <button onClick={handboradlist}>보더 리스트</button>
                            </Link>
                        </div>

                    )}


                    {/* 섹션 목록 표시 */}
                    <div className="sections-container">
                        {sections.map((section) => (
                            <div key={section.sectionId} className="section-card">
                                <div>
                                    <h4>{section.contents}</h4>
                                    <p>작성자: {section.nickname}</p>
                                    <p>작성일: {section.createdAt}</p>
                                    <p>수정일: {section.modifiedAt}</p>
                                    {/* 삭제 버튼 */}
                                    <button onClick={() => handleDeleteSession(section.sectionId)} className="button">섹션 삭제</button>
                                    {/* 카드 추가 버튼 */}
                                    <button onClick={() => openModal(section.sectionId)} className="button">카드 추가</button>
                                    {/* 카드 목록 표시 */}
                                    {section.cards && section.cards.map((card) => (
                                        <div key={card.cardId} className="card-item">
                                            {/* 카드 상세 페이지로 이동하는 클릭 이벤트 추가 */}
                                            <h5 onClick={() => navigateToCardDetail(card.cardId)} className="card-title">{card.title}</h5>
                                            <p>{card.contents}</p>
                                            <p>작성자: {card.nickname}</p>
                                            <p>작성일: {card.createdAt}</p>
                                            <p>수정일: {card.modifiedAt}</p>
                                            {/* 카드 삭제 버튼 */}
                                            <button onClick={() => handleDeleteCard(card.cardId)} className="button">카드 삭제</button>
                                        </div>
                                    ))}
                                </div>
                                {/* 모달 창 */}
                                {showModal && selectedSectionId === section.sectionId && (
                                    <CardFormModal
                                        onSubmit={handleAddCard}
                                        onClose={closeModal}
                                    />
                                )}
                            </div>
                        ))}
                    </div>

                </div>
            )}
        </div>
    );
}

export default BoardDetail;
