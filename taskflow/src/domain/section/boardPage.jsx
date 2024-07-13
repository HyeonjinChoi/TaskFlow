import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { Link } from 'react-router-dom'; // react-router-dom에서 Link import

function BoardPage({ onLogout }) {
    const [boardId, setBoardId] = useState(null);
    const [sections, setSections] = useState([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const storedBoardId = localStorage.getItem('board');
        setBoardId(storedBoardId);
        console.log(`Loaded board id from localStorage: ${storedBoardId}`);
        const token = localStorage.getItem('Authorization');

        const fetchSections = async () => {
            try {
                const response = await axios.get('http://localhost:8080/api/sections', {
                    headers: {
                        Authorization: token // 토큰을 헤더에 포함해서 보냄
                    },
                    params: {
                        boardId: storedBoardId,
                        page: 0
                    }
                });
                console.log('Fetched sections:', response.data.data);
                setSections(response.data.data.content);
                setLoading(false);
            } catch (error) {
                console.error('Failed to fetch sections:', error);
                setLoading(false);
            }
        };

        if (storedBoardId) {
            fetchSections();
        }
    }, []);

    const handleLogout = () => {
        localStorage.removeItem('board');
        onLogout();
    };

    const handleDeleteBoardKey = () => {
        localStorage.removeItem('board');
        setBoardId(null);
        setSections([]); // 섹션 초기화
        setLoading(true); // 로딩 상태 초기화
    };

    return (
        <div>
            <header>
                <h1>Board Page</h1>
                <button onClick={handleLogout}>Logout</button>
                <Link to="/board">
                    <button onClick={handleDeleteBoardKey}>go Board</button>
                </Link>
            </header>
            <main>
            <h2>Board ID: {boardId}</h2>
                {loading ? (
                    <p>Loading...</p>
                ) : (
                    <div>
                        {sections.map(section => (
                            <div key={section.id} style={styles.box}>
                                <p>Section ID: {section.contents}</p>
                                <p>Section ID: {section.nickname}</p>
                                <p>Name: {section.createdAt}</p>
                                <p>Name: {section.createdAt}</p>
                                <p>Name: {section.position}</p>
                            </div>
                        ))}
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
    },
};

export default BoardPage;
