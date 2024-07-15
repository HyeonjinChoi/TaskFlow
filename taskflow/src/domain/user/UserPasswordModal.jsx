import React, { useState } from 'react';

const UserPasswordModal = ({ onSubmit, onClose }) => {
    const [currentPassword, setcurrentPassword] = useState('');
    const [newPassword, setnewPassword] = useState('');

    const handleSubmit = () => {
        onSubmit(currentPassword, newPassword);
        onClose(); // 모달 창 닫기
    };

    return (
        <div className="modal">
            <div className="modal-content">
                <h3 className="cards">비밀번호 변경<span className="close" onClick={onClose}>&times;</span></h3>
                <div><label>
                    현재 비밀번호:
                    <input
                        type="password"
                        value={currentPassword}
                        onChange={(e) => setcurrentPassword(e.target.value)}
                    />
                </label></div>
                <div><label>
                    새로운 비밀번호:
                    <input
                        type="password"
                        value={newPassword}
                        onChange={(e) => setnewPassword(e.target.value)}
                    />
                </label></div>
                <button onClick={handleSubmit}>추가</button>
            </div>
        </div>
    );
};



export default UserPasswordModal;
