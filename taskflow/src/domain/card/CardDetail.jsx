import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { useParams, useNavigate } from 'react-router-dom';
import './CardDetail.css';

function CardDetail() {
    const { cardId } = useParams();
    const navigate = useNavigate();
    const [card, setCard] = useState(null);
    const [comments, setComments] = useState([]);
    const [loading, setLoading] = useState(true);
    const [newComment, setNewComment] = useState('');

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

        const fetchComments = async () => {
            try {
                const token = localStorage.getItem('Authorization');
                const response = await axios.get(`http://localhost:8080/api/comments`, {
                    params: { cardId: cardId, page: 0 },
                    headers: { Authorization: token }
                });
                console.log('댓글 가져오기 성공:', response.data);

                const mappedComments = response.data.data.map(comment => ({
                    commentId: comment.commentId,
                    contents: comment.contents,
                    createdAt: comment.createdAt,
                    modifiedAt: comment.modifiedAt,
                    nickname: comment.nickname // 닉네임 추가
                }));

                setComments(mappedComments);
            } catch (error) {
                console.error('댓글 가져오기 실패:', error);
            }
        };

        fetchCardDetail();
        fetchComments();
    }, [cardId]);

    const handleAddComment = async () => {
        try {
            const token = localStorage.getItem('Authorization');
            const response = await axios.post('http://localhost:8080/api/comments', {
                contents: newComment,
                cardId: cardId
            }, {
                headers: { Authorization: token }
            });
            console.log('댓글 추가 성공:', response.data);
            setComments([...comments, response.data.data]);
            setNewComment('');
        } catch (error) {
            console.error('댓글 추가 실패:', error);
        }
    };

    const handleDeleteComment = async (commentId) => {
        try {
            const token = localStorage.getItem('Authorization');
            const dtoReq = { cardId: cardId };
            await axios.delete(`http://localhost:8080/api/comments/${commentId}`, {
                headers: { Authorization: token },
                data: dtoReq
            });
            console.log('댓글 삭제 성공');
            setComments(comments.filter(comment => comment.commentId !== commentId));
        } catch (error) {
            console.error('댓글 삭제 실패:', error);
        }
    };

    const handleUpdateComment = async (commentId, updatedContents) => {
        try {
            const token = localStorage.getItem('Authorization');
            const response = await axios.put(`http://localhost:8080/api/comments/${commentId}`, {
                contents: updatedContents,
                cardId: cardId
            }, {
                headers: { Authorization: token }
            });
            console.log('댓글 수정 성공:', response.data);
            setComments(comments.map(comment => comment.commentId === commentId ? response.data.data : comment));
        } catch (error) {
            console.error('댓글 수정 실패:', error);
        }
    };

    if (loading) {
        return <p>로딩 중...</p>;
    }

    if (!card) {
        return <p>카드를 찾을 수 없습니다.</p>;
    }

    return (
        <div className="card-detail-container">
            <div className="card-detail-header">
                <h2>카드 상세 페이지</h2>
                <h3>{card.title}</h3>
                <p>{card.contents}</p>
                <p>작성자: {card.nickname}</p>
                <p>작성일: {card.createdAt}</p>
                <p>수정일: {card.modifiedAt}</p>
            </div>

            <div className="card-detail-comments">
                <h3>댓글</h3>
                {comments.map(comment => (
                    <div key={comment.commentId} className="comment-item">
                        <p>{comment.contents}</p>
                        <p>작성자: {comment.nickname}</p>
                        <p>작성일: {comment.createdAt}</p>
                        <p>수정일: {comment.modifiedAt}</p>
                        <div className="comment-item-buttons">
                            <button className="delete-button" onClick={() => handleDeleteComment(comment.commentId)}>댓글 삭제</button>
                            <button onClick={() => {
                                const updatedContents = prompt('댓글 수정', comment.contents);
                                if (updatedContents) handleUpdateComment(comment.commentId, updatedContents);
                            }}>댓글 수정</button>
                        </div>
                    </div>
                ))}
                <div className="comment-input-container">
                    <input
                        type="text"
                        value={newComment}
                        onChange={(e) => setNewComment(e.target.value)}
                        placeholder="댓글 입력"
                    />
                    <button onClick={handleAddComment}>댓글 추가</button>
                </div>
            </div>
        </div>
    );
}

export default CardDetail;
