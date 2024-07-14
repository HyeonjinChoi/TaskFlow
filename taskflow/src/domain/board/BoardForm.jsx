import React, { useState } from 'react';
import axios from 'axios';

function BoardForm({ onClose }) {
    const [board, setBoard] = useState({
        name: '',
        description: ''
    });

    const handleChange = (e) => {
        const { name, value } = e.target;
        setBoard({ ...board, [name]: value });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();

        try {
            const token = localStorage.getItem('Authorization');
            await axios.post('http://localhost:8080/api/boards', board, {
                headers: {
                    Authorization: token
                }
            });
            console.log('Board added successfully!');
            onClose(true); // 팝업창 닫기 및 부모 컴포넌트에 변경 사항 알리기
        } catch (error) {
            console.error('Failed to add board:', error);
        }
    };

    return (
        <div style={styles.overlay}>
            <div style={styles.popup}>
                <h2>보드 추가</h2>
                <form onSubmit={handleSubmit}>
                    <label>
                        Name:
                        <input type="text" name="name" value={board.name} onChange={handleChange} required />
                    </label>
                    <br />
                    <label>
                        Description:
                        <input type="text" name="description" value={board.description} onChange={handleChange} required />
                    </label>
                    <br />
                    <button type="submit">Submit</button>
                    <button type="button" onClick={() => onClose(false)}>Close</button> {/* 취소 버튼 */}
                </form>
            </div>
        </div>
    );
}

const styles = {
    overlay: {
        position: 'fixed',
        top: 0,
        left: 0,
        right: 0,
        bottom: 0,
        backgroundColor: 'rgba(0, 0, 0, 0.5)',
        display: 'flex',
        justifyContent: 'center',
        alignItems: 'center'
    },
    popup: {
        backgroundColor: '#fff',
        padding: '20px',
        borderRadius: '5px',
        boxShadow: '0 2px 10px rgba(0, 0, 0, 0.1)'
    }
};

export default BoardForm;
