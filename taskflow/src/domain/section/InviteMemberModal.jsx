import React, { useState } from 'react';
import './InviteMemberModal.css';

function InviteMemberModal({ isOpen, onClose, onSubmit }) {
    const [username, setUsername] = useState('');

    const handleInvite = () => {
        onSubmit(username);
        setUsername('');
    };

    if (!isOpen) return null;

    return (
        <div className="modal-overlay">
            <div className="modal-content">
                <h2>회원 초대</h2>
                <input
                    type="text"
                    value={username}
                    onChange={(e) => setUsername(e.target.value)}
                    placeholder="초대할 사용자 이름 입력"
                    className="invite-input"
                />
                <button onClick={handleInvite} className="button">초대</button>
                <button onClick={onClose} className="button">취소</button>
            </div>
        </div>
    );
}

export default InviteMemberModal;
