// Board.jsx

import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { Link } from 'react-router-dom';
import BoardForm from './BoardForm'; // BoardForm 컴포넌트 import

function Board({ onLogout }) {
    const [boards, setBoards] = useState([]); // 보드 데이터 상태
    const [loading, setLoading] = useState(true); // 데이터 로딩 상태
    const [showPopup, setShowPopup] = useState(false); // 팝업 모달 상태

    const fetchBoardData = async () => {
        const token = localStorage.getItem('Authorization');
        console.log('저장된 토큰:', token);
        try {
            // API 호출하여 보드 데이터 가져오기
            const response = await axios.get('http://localhost:8080/api/boards', {
                headers: {
                    Authorization: token // 토큰을 헤더에 포함해서 보냄
                },
                params: {
                    page: 0
                }

            });
            console.log('보드 데이터 가져오기 성공:', response.data);
            setBoards(response.data.data.content); // 가져온 보드 데이터를 상태에 저장
            setLoading(false); // 로딩 상태 변경
        } catch (error) {
            console.error('보드 데이터 가져오기 실패:', error);
            setLoading(false); // 에러 발생 시에도 로딩 상태 변경
        }
    };

    useEffect(() => {
        fetchBoardData();
    }, []); // 빈 배열을 전달하여 한 번만 실행되도록 설정

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
        setShowPopup(true); // 보더 추가하기 버튼을 누르면 팝업창을 열기
    };

    const handleClosePopup = () => {
        setShowPopup(false); // 팝업창을 닫을 때
        fetchBoardData(); // 팝업창 닫을 때 보드 목록 다시 가져오기
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
                await fetchBoardData();
            } catch (error) {
                console.error('보드 삭제 실패:', error);
                // 에러 처리
            }
        }
    };

    return (
        <div>
            <header>
                <h1>보더 페이지</h1>
                <Link to="/">
                    <button onClick={handleLogout}>로그아웃</button>
                </Link>
                <button onClick={handleAddBoard}>보드 추가</button>
            </header>
            <main style={styles.boardContainer}>
                {loading ? (
                    <p>로딩 중...</p>
                ) : (
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
                                    {/* 삭제 버튼 */}
                                    <button onClick={() => handleDeleteBoard(board.id)}>삭제</button>
                                </div>
                            ))
                        ) : (
                            <p>사용 가능한 보드가 없습니다.</p>
                        )}
                    </div>
                )}
            </main>
            {showPopup && (
                <BoardForm onClose={handleClosePopup} /> // 팝업창 컴포넌트 렌더링
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
