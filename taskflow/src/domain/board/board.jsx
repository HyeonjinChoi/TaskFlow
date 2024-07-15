import React, { useState, useEffect, useCallback } from 'react';
import axios from 'axios';
import { Link } from 'react-router-dom';
import BoardForm from './BoardForm'; // BoardForm 컴포넌트 import

function Board({ onLogout }) {
    const [boards, setBoards] = useState([]); // 보드 데이터 상태
    const [loading, setLoading] = useState(true); // 데이터 로딩 상태
    const [showPopup, setShowPopup] = useState(false); // 팝업 모달 상태
    const [editBoard, setEditBoard] = useState(null); // 수정할 보드 상태
    const [isEditMode, setIsEditMode] = useState(false); // 수정 모드 상태
    const [page, setPage] = useState(0); // 현재 페이지 상태
    const [hasMore, setHasMore] = useState(true); // 추가 데이터 여부 상태
    const [fetching, setFetching] = useState(false); // 데이터 가져오는 중 여부 상태

    const fetchBoardData = async (newPage = 0) => {
        if (fetching) return; // 이미 데이터를 가져오고 있는 중이면 중복 요청 방지

        const token = localStorage.getItem('Authorization');
        console.log('저장된 토큰:', token);
        try {
            setFetching(true); // 데이터 가져오는 중으로 설정

            // API 호출하여 보드 데이터 가져오기
            const response = await axios.get('http://localhost:8080/api/boards', {
                headers: {
                    Authorization: token // 토큰을 헤더에 포함해서 보냄
                },
                params: {
                    page: newPage
                }
            });
            console.log('보드 데이터 가져오기 성공:', response.data);

            if (response.data.data.content.length === 0) {
                setHasMore(false); // 가져올 데이터가 없으면 더 이상 가져올 필요 없음
            } else {
                setBoards(prevBoards => [...prevBoards, ...response.data.data.content]); // 기존 보드 데이터에 추가
                setPage(newPage + 1); // 페이지 증가
            }

            setLoading(false); // 로딩 상태 변경
            setFetching(false); // 데이터 가져오는 중 해제
        } catch (error) {
            console.error('보드 데이터 가져오기 실패:', error);
            setLoading(false); // 에러 발생 시에도 로딩 상태 변경
            setFetching(false); // 데이터 가져오는 중 해제
        }
    };

    useEffect(() => {
        fetchBoardData(page); // 초기 페이지 로드
    }, []); // 컴포넌트 마운트 시 한 번만 실행

    const handleBoardClick = (id) => {
        // 보드 ID를 로컬 스토리지에 저장
        localStorage.setItem('board', id);
        console.log(`저장된 보드 ID: ${id}`);
    };

    const handleLogout = () => {
        localStorage.removeItem('board');
        onLogout();
    };

    const handleAddBoard = () => {
        setEditBoard(null); // 추가 모드일 때는 보드 데이터가 없으므로 null 설정
        setIsEditMode(false); // 추가 모드로 설정
        setShowPopup(true); // 팝업창 열기
    };

    const handleClosePopup = (isDataChanged) => {
        setShowPopup(false); // 팝업창을 닫을 때
        if (isDataChanged) {
            setBoards([]); // 보드 데이터를 초기화하고
            setPage(0); // 페이지를 초기화하여 데이터를 새로 가져오기
        }
    };

    const handleDeleteBoard = async (boardId) => {
        const confirmDelete = window.confirm("정말로 이 보드를 삭제하시겠습니까?");
        if (confirmDelete) {
            const token = localStorage.getItem('Authorization');
            try {
                await axios.delete(`http://localhost:8080/api/boards/${boardId}`, {
                    headers: {
                        Authorization: token
                    }
                });
                // 삭제 성공 시, 업데이트된 보드 목록 다시 가져오기
                setBoards([]);
                setPage(0);
            } catch (error) {
                console.error('보드 삭제 실패:', error.response.data.message);
                // 에러 처리
                alert(error.response.data.message);
            }
        }
    };

    const handleEditBoard = (board) => {
        setEditBoard(board); // 수정할 보드 설정
        setIsEditMode(true); // 수정 모드로 설정
        setShowPopup(true); // 팝업창 열기
    };

    const handleScroll = useCallback(() => {
        if (window.innerHeight + document.documentElement.scrollTop !== document.documentElement.offsetHeight || fetching || !hasMore) return;
        fetchBoardData(page); // 추가 데이터를 가져오는 함수 호출
    }, [fetching, hasMore, page]); // 필요한 상태 변수들을 의존성 배열에 추가

    useEffect(() => {
        window.addEventListener('scroll', handleScroll);
        return () => window.removeEventListener('scroll', handleScroll);
    }, [handleScroll]);

    // 스크롤이 맨 아래로 내려갔을 때 추가 데이터를 가져오기 위한 로직
    useEffect(() => {
        const handleScrollToBottom = () => {
            if ((window.innerHeight + window.scrollY) >= document.body.offsetHeight) {
                fetchBoardData(page); // 페이지를 증가시켜 추가 데이터를 가져옴
            }
        };

        window.addEventListener('scroll', handleScrollToBottom);
        return () => window.removeEventListener('scroll', handleScrollToBottom);
    }, [fetchBoardData, page]); // fetchBoardData와 page를 의존성 배열에 추가

    return (
        <div>
            <header>
                <h1>보더 페이지</h1>
                <Link to="/">
                    <button onClick={handleLogout}>로그아웃</button>
                </Link>
                <Link to="/userProfile">
                    <button>유저 프로필</button>
                </Link>
                <button onClick={handleAddBoard}>보드 추가</button>
            </header>
            <main style={styles.boardContainer}>
                <div style={styles.boardGrid}>
                    {boards.length > 0 ? (
                        boards.map(board => (
                            <div key={board.id} style={styles.boardItem}>
                                {/* Link를 사용하여 클릭 시 boardDetail 페이지로 이동하도록 설정 */}
                                <Link to={`/boardDetail/${board.id}`} style={{ textDecoration: 'none', color: 'inherit' }}>
                                    <h2 onClick={() => handleBoardClick(board.id)}>{board.name}</h2>
                                </Link>
                                <p>{board.description}</p>
                                <p>작성일: {board.createdAt}</p>
                                <p>수정일: {board.modifiedAt}</p>
                                {/* 수정 버튼 */}
                                <button onClick={() => handleEditBoard(board)}>수정</button>
                                {/* 삭제 버튼 */}
                                <button onClick={() => handleDeleteBoard(board.id)}>삭제</button>
                            </div>
                        ))
                    ) : (
                        <p>사용 가능한 보드가 없습니다.</p>
                    )}
                    {loading && <p>로딩 중...</p>}
                </div>
            </main>
            {showPopup && (
                <BoardForm
                    onClose={handleClosePopup}
                    board={editBoard}
                    mode={isEditMode ? 'edit' : 'add'} // 수정 모드일 때 'edit' 전달
                />
            )}
        </div>
    );
}

const styles = {
    boardContainer: {
        display: 'flex',
        flexDirection: 'column',
        alignItems: 'center',
    },
    boardGrid: {
        display: 'grid',
        gridTemplateColumns: 'repeat(4, 1fr)',
        gap: '20px',
        maxWidth: '1200px',
        margin: '0 auto',
    },
    boardItem: {
        border: '1px solid #ccc',
        padding: '10px',
        borderRadius: '5px',
        cursor: 'pointer',
    },
};

export default Board;
