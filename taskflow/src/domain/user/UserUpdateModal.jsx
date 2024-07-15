import React, { useState } from 'react';

const UserUpdateModal = ({ onSubmit, onClose }) => {
    const [nickname, setnickname] = useState('');
    const [introduction, setintroduction] = useState('');

    const handleSubmit = () => {
        onSubmit(nickname, introduction);
        onClose(); // 모달 창 닫기
    };

    return (
        <div className="modal">
            <div className="modal-content">
                <h3 className="cards">프로필 수정<span className="close" onClick={onClose}>&times;</span></h3>
                <div><label>
                    닉네임:
                    <input
                        type="text"
                        value={nickname}
                        onChange={(e) => setnickname(e.target.value)}
                    />
                </label></div>
                <div><label>
                    한줄 설명:
                    <textarea
                        value={introduction}
                        onChange={(e) => setintroduction(e.target.value)}
                    />
                </label></div>
                <button onClick={handleSubmit}>추가</button>
            </div>
        </div>
    );
};



export default UserUpdateModal;
