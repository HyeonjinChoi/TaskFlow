import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { Link } from 'react-router-dom';

function BoardPage({ onLogout }) {
    const [boardId, setBoardId] = useState(null);
    const [sections, setSections] = useState([]);
    const [loadingSections, setLoadingSections] = useState(true);

    useEffect(() => {
        const storedBoardId = localStorage.getItem('board');
        setBoardId(storedBoardId);
        console.log(`Loaded board id from localStorage: ${storedBoardId}`);
        const token = localStorage.getItem('Authorization');

        const fetchSections = async () => {
            try {
                const response = await axios.get('http://localhost:8080/api/sections', {
                    headers: {
                        Authorization: token
                    },
                    params: {
                        boardId: storedBoardId,
                        page: 0
                    }
                });
                console.log('Fetched sections:', response.data.data);
                setSections(response.data.data.content);
                setLoadingSections(false);
            } catch (error) {
                console.error('Failed to fetch sections:', error);
                setLoadingSections(false);
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
        setSections([]);
        setLoadingSections(true);
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
                {loadingSections ? (
                    <p>Loading sections...</p>
                ) : (
                    <div>
                        {sections.map(section => (
                            <div key={section.id} style={styles.box}>
                                <p>Section Contents: {section.contents}</p>
                                <p>Section Nickname: {section.nickname}</p>
                                <p>Section Position: {section.position}</p>
                                <p>Created At: {section.createdAt}</p>
                                <p>Modified At: {section.modifiedAt}</p>

                                <FetchCards sectionId={section.id} boardId={boardId} key={section.id} />
                            </div>
                        ))}
                    </div>
                )}
            </main>
        </div>
    );
}

const FetchCards = ({ sectionId, boardId }) => {
    const [cards, setCards] = useState([]);
    const [loadingCards, setLoadingCards] = useState(true);

    useEffect(() => {
        const token = localStorage.getItem('Authorization');

        const fetchCards = async () => {
            try {
                const response = await axios.get('http://localhost:8080/api/cards', {
                    headers: {
                        Authorization: token
                    },
                    params: {
                        boardId: boardId,
                        sectionId: sectionId, // Ensure sectionId is passed to API call
                        page: 0
                    }
                });
                console.log(`Fetched cards for section ${sectionId}:`, response.data.data);
                setCards(response.data.data.content);
                setLoadingCards(false);
            } catch (error) {
                console.error(`Failed to fetch cards for section ${sectionId}:`, error);
                setLoadingCards(false);
            }
        };

        fetchCards();
    }, [boardId, sectionId]); // Ensure dependencies are properly listed

    return (
        <div>
            {loadingCards ? (
                <p>Loading cards...</p>
            ) : (
                <div>
                    {cards.map(card => (
                        <div key={card.id} style={styles.cardBox}>
                            <h3>{card.title}</h3>
                            <p>Contents: {card.contents}</p>
                            <p>Nickname: {card.nickname}</p>
                            <p>Position: {card.position}</p>
                            <p>Created At: {card.createdAt}</p>
                            <p>Modified At: {card.modifiedAt}</p>
                        </div>
                    ))}
                </div>
            )}
        </div>
    );
};

const styles = {
    box: {
        border: '1px solid #ccc',
        padding: '10px',
        margin: '10px 0',
        borderRadius: '5px',
    },
    cardBox: {
        border: '1px solid #ccc',
        padding: '10px',
        margin: '10px 0',
        borderRadius: '5px',
        backgroundColor: '#f9f9f9',
    },
};

export default BoardPage;
