import React, { useState } from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import LoginForm from './domain/auth/login'; // LoginForm 컴포넌트 import
import Board from './domain/board/board'; // Board 컴포넌트 import
import BoardPage from './domain/section/boardPage'; // BoardPage 컴포넌트 import

function App() {
    const [isLoggedIn, setIsLoggedIn] = useState(false);

    const handleLogin = () => {
        setIsLoggedIn(true);
    };

    const handleLogout = () => {
        setIsLoggedIn(false);
        localStorage.removeItem('Authorization'); // 토큰 삭제
    };

    return (
        <Router>
            <Routes>
                <Route path="/" element={isLoggedIn ? <Navigate to="/board" /> : <LoginForm onLogin={handleLogin} />} />
                <Route path="/board" element={<Board onLogout={handleLogout} />} />
                <Route path="/boardPage" element={<BoardPage onLogout={handleLogout} />} />
            </Routes>
        </Router>
    );
}

export default App;
