import React, { useState } from 'react';
import CardForm from './CardForm'; // CardForm 컴포넌트 import
import './CardFormModal.css'; // CSS 파일 import


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
                <h3 className="cards">카드 추가<span className="close" onClick={onClose}>&times;</span></h3>
                <div><label>
                    제목:
                    <input
                        type="text"
                        value={title}
                        onChange={(e) => setTitle(e.target.value)}
                    />
                </label></div>
                <div><label>
                    내용:
                    <textarea
                        value={contents}
                        onChange={(e) => setContents(e.target.value)}
                    />
                </label></div>
                <div><label>
                    마감일:
                    <input
                        type="datetime-local"
                        value={dueDate}
                        onChange={(e) => setDueDate(e.target.value)}
                    />
                </label></div>
                <button onClick={handleSubmit}>추가</button>
            </div>
        </div>
    );
};



export default CardFormModal;
