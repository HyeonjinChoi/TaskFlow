import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { Link } from 'react-router-dom';

function Board({ onLogout }) {
    const [boards, setBoards] = useState([]); // 보드 데이터 상태
    const [loading, setLoading] = useState(true); // 데이터 로딩 상태

    useEffect(() => {
        const fetchBoardData = async () => {
            const token = localStorage.getItem('Authorization');
            console.log('Stored token:', token);
            try {
                // API 호출하여 보드 데이터 가져오기
                const response = await axios.get('http://localhost:8080/api/boards', {
                    headers: {
                        Authorization: token // 토큰을 헤더에 포함해서 보냄
                    }
                });
                console.log('Fetched board data:', response.data);
                setBoards(response.data.data); // 가져온 보드 데이터를 상태에 저장
                setLoading(false); // 로딩 상태 변경
            } catch (error) {
                console.error('Failed to fetch board data:', error);
                setLoading(false); // 에러 발생 시에도 로딩 상태 변경
            }
        };

        fetchBoardData(); // 컴포넌트가 마운트될 때 데이터 가져오기

        // Clean-up 함수 (선택 사항)
        return () => {
            // 만약 컴포넌트가 언마운트될 때 API 요청을 취소해야 할 경우 여기에 추가할 수 있음
        };
    }, []); // 빈 배열을 전달하여 한 번만 실행되도록 설정

    const handleBoardClick = (id) => {
        // 보드 ID를 로컬 스토리지에 저장
        localStorage.setItem('board', id);
        console.log(`Stored board id: ${id}`);
    };

    return (
        <div>
            <header>
                <h1>보더 페이지</h1>
                <button onClick={onLogout}>Logout</button>
            </header>
            <main>
                {loading ? (
                    <p>Loading...</p>
                ) : (
                    <div>
                        {boards.length > 0 ? (
                            boards.map(board => (
                                <div key={board.id} style={styles.box}>
                                    {/* Link를 사용하여 클릭 시 boardPage로 이동하도록 설정 */}
                                    <Link to="/boardPage" style={{ textDecoration: 'none', color: 'inherit' }}>
                                        <h2 onClick={() => handleBoardClick(board.id)}>{board.name}</h2>
                                    </Link>
                                    <p>{board.description}</p>
                                    <p>Created At: {board.createdAt}</p>
                                    <p>Modified At: {board.modifiedAt}</p>
                                </div>
                            ))
                        ) : (
                            <p>No boards available</p>
                        )}
                    </div>
                )}
            </main>
        </div>
    );
}

const styles = {
    box: {
        border: '1px solid #ccc',
        padding: '10px',
        margin: '10px 0',
        borderRadius: '5px',
        cursor: 'pointer'
    },
};

export default Board;
