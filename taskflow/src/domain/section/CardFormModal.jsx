import React, { useState } from 'react';
import CardForm from './CardForm'; // CardForm 컴포넌트 import

const CardFormModal = ({ onSubmit, onClose }) => {
    const [title, setTitle] = useState('');
    const [contents, setContents] = useState('');
    const [dueDate, setDueDate] = useState('');

    const handleSubmit = () => {
        onSubmit(title, contents, dueDate);
        onClose(); // 모달 창 닫기
    };

    return (
        <div className="modal">
            <div className="modal-content">
                <span className="close" onClick={onClose}>&times;</span>
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
            </div>
        </div>
    );
};

export default CardFormModal;
