import React, { useState } from 'react';

const SignoutModal = ({ onSubmit, onClose }) => {
    const [password, setPassword] = useState('');

    const handleSubmit = () => {
        onSubmit(password);
        onClose();
    };

    return (
        <div className="modal">
            <div className="modal-content">
                <h2>회원 탈퇴</h2>
                <p>회원 탈퇴를 위해 비밀번호를 입력해주세요.</p>
                <input
                    type="password"
                    value={password}
                    onChange={(e) => setPassword(e.target.value)}
                    placeholder="비밀번호"
                />
                <button className="btn btn-danger" onClick={handleSubmit}>회원 탈퇴</button>
                <button className="btn btn-secondary" onClick={onClose}>취소</button>
            </div>
        </div>
    );
};

export default SignoutModal;
