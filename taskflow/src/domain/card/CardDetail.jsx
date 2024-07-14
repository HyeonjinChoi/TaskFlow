import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { useParams } from 'react-router-dom';

function CardDetail() {
    const { cardId } = useParams();
    const [card, setCard] = useState(null);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const fetchCardDetail = async () => {
            try {
                const token = localStorage.getItem('Authorization');
                const response = await axios.get(`http://localhost:8080/api/cards/${cardId}`, {
                    headers: { Authorization: token }
                });
                console.log('카드 상세 정보 가져오기 성공:', response.data);
                setCard(response.data.data);
            } catch (error) {
                console.error('카드 상세 정보 가져오기 실패:', error);
            } finally {
                setLoading(false);
            }
        };

        fetchCardDetail();
    }, [cardId]);

    if (loading) {
        return <p>로딩 중...</p>;
    }

    if (!card) {
        return <p>카드를 찾을 수 없습니다.</p>;
    }

    return (
        <div>
            <h2>카드 상세 페이지</h2>
            <h3>{card.title}</h3>
            <p>{card.contents}</p>
            <p>작성자: {card.nickname}</p>
            <p>작성일: {card.createdAt}</p>
            <p>수정일: {card.modifiedAt}</p>
        </div>
    );
}

export default CardDetail;
