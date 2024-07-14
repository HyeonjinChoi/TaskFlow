import React, { useState } from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate, useNavigate } from 'react-router-dom';
import LoginForm from './domain/auth/login'; // LoginForm 컴포넌트 import
import Board from './domain/board/board'; // Board 컴포넌트 import
import SignupForm from "./domain/auth/SignupForm"; // BoardPage 컴포넌트 import
import BoardDetail from './domain/section/BoardDetail';// BoardDetail 컴포넌트 import
import CardForm from './domain/section/CardForm';
import CardDetail from "./domain/card/CardDetail";

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
                <Route path="/login" element={<LoginForm onLogin={handleLogin} />} />
                <Route path="/signup" element={<SignupForm />} />
                <Route path="/board" element={<Board onLogout={handleLogout} />} />
                <Route path="/boardDetail/:boardId" element={<BoardDetail />} />
                <Route path="/cardForm"  element={<CardForm />} />
                <Route path="/cards/:cardId" element={<CardDetail/>} />
            </Routes>
        </Router>
    );
}

export default App;
