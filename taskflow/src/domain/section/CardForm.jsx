import React, { useState } from 'react';

const CardForm = ({ onClose, onSubmit }) => {
    const [title, setTitle] = useState('');
    const [contents, setContents] = useState('');
    const [dueDate, setDueDate] = useState('');

    const handleSubmit = () => {
        onSubmit(title, contents, dueDate);
        window.close();
    };

    return (
        <div>
            <h3>카드 추가</h3>
            <label>
                제목:
                <input
                    type="text"
                    value={title}
                    onChange={(e) => setTitle(e.target.value)}
                />
            </label>
            <label>
                내용:
                <textarea
                    value={contents}
                    onChange={(e) => setContents(e.target.value)}
                />
            </label>
            <label>
                마감일:
                <input
                    type="datetime-local"
                    value={dueDate}
                    onChange={(e) => setDueDate(e.target.value)}
                />
            </label>
            <button onClick={handleSubmit}>추가</button>
            <button onClick={onClose}>닫기</button>
        </div>
    );
};

export default CardForm;
