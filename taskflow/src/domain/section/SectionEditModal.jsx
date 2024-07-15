import React, { useState } from 'react';
import './SectionEditModal.css';

function SectionEditModal({ section, onSubmit, onClose }) {
    const [contents, setContents] = useState(section.contents);

    const handleSubmit = (e) => {
        e.preventDefault();
        onSubmit(section.sectionId, contents);
    };

    return (
        <div className="modal">
            <div className="modal-content">
                <h2>섹션 수정</h2>
                <form onSubmit={handleSubmit}>
                    <label>
                        내용:
                        <input
                            type="text"
                            value={contents}
                            onChange={(e) => setContents(e.target.value)}
                        />
                    </label>
                    <button type="submit">수정</button>
                    <button type="button" onClick={onClose}>취소</button>
                </form>
            </div>
        </div>
    );
}

export default SectionEditModal;
