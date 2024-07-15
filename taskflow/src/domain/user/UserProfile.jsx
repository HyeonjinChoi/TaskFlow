import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import axiosInstance from '../../api/axiosInstance';
import UserUpdateModal from './UserUpdateModal'; // 수정할 모달 컴포넌트 import
import UserPasswordModal from './UserPasswordModal'; // 비밀번호 변경 모달 컴포넌트 import
import SignoutModal from './SignoutModal'; // 회원 탈퇴 모달 컴포넌트 import

const UserProfile = () => {
    const [profileData, setProfileData] = useState(null);
    const [showUpdate, setShowUpdate] = useState(false); // 수정 모달 표시 여부 상태
    const [showPassword, setShowPassword] = useState(false); // 비밀번호 변경 모달 표시 여부 상태
    const [showSignout, setShowSignout] = useState(false); // 회원 탈퇴 모달 표시 여부 상태

    useEffect(() => {
        fetchProfile();
    }, []);

    const navigate = useNavigate();

    const goToBoard = () => {
        navigate('/board');
    };

    const fetchProfile = async () => {
        const token = localStorage.getItem('Authorization');
        try {
            const response = await axiosInstance.get('/api/users', {
                headers: {
                    Authorization: token
                }
            });
            setProfileData(response.data.data);
            console.log('프로필 조회 성공:', response.data);
        } catch (error) {
            console.error('프로필 조회 실패:', error);
        }
    };

    const handleUpdateClick = () => {
        setShowUpdate(true); // 수정 모달을 보이도록 설정
    };

    const handlePasswordClick = () => {
        setShowPassword(true); // 비밀번호 변경 모달을 보이도록 설정
    };

    const handleSignoutClick = () => {
        setShowSignout(true); // 회원 탈퇴 모달을 보이도록 설정
    };

    const handleUpdateProfile = async (nickname, introduction) => {
        try {
            const token = localStorage.getItem('Authorization');
            const response = await axiosInstance.put(`/api/users`, {
                nickname: nickname,
                introduction: introduction,
            }, {
                headers: { Authorization: token }
            });
            console.log('프로필 수정:', response.data);
            fetchProfile(); // 프로필 업데이트 후 다시 가져오기
        } catch (error) {
            console.error('프로필 수정 실패:', error);
            const errorMessage = JSON.parse(error.request.responseText).message;
            alert(`${errorMessage}`);
        }
    };

    const handleUpdatePassword = async (currentPassword, newPassword) => {
        try {
            const token = localStorage.getItem('Authorization');
            const response = await axiosInstance.put(`/api/users/password`, {
                currentPassword: currentPassword,
                newPassword: newPassword,
            }, {
                headers: { Authorization: token }
            });
            console.log('비밀번호 변경:', response.data);
            fetchProfile(); // 비밀번호 변경 후 다시 가져오기
        } catch (error) {
            console.error('비밀번호 변경 실패:', error);
            const errorMessage = JSON.parse(error.request.responseText).message;
            alert(`${errorMessage}`);
        }
    };

    const handleSignout = async (password) => {
        try {
            const token = localStorage.getItem('Authorization');
            const response = await axiosInstance.put(`/api/auth/signout`, {
                password: password
            }, {
                headers: { Authorization: token }
            });
            console.log('회원 탈퇴:', response.data);
            // 로그아웃 처리 및 홈 페이지로 이동
            localStorage.clear();
            navigate('/');
        } catch (error) {
            console.error('회원 탈퇴 실패:', error);
            const errorMessage = JSON.parse(error.request.responseText).message;
            alert(`${errorMessage}`);
        }
    };

    return (
        <div className="container mt-5">
            <h1>프로필 조회</h1>
            {profileData ? (
                <div>
                    <p>이름: {profileData.nickname}</p>
                    <p>한줄소개: {profileData.introduction}</p>
                    {/* 기타 프로필 정보 */}
                </div>
            ) : (
                <p>프로필을 불러오는 중입니다...</p>
            )}
            <button className="btn btn-primary" onClick={handleUpdateClick}>프로필 수정</button>
            {showUpdate && (
                <UserUpdateModal
                    profileData={profileData}
                    onSubmit={handleUpdateProfile}
                    onClose={() => setShowUpdate(false)}
                />
            )}
            <button className="btn btn-primary" onClick={handlePasswordClick}>비밀번호 수정</button>
            {showPassword && (
                <UserPasswordModal
                    profileData={profileData}
                    onSubmit={handleUpdatePassword}
                    onClose={() => setShowPassword(false)}
                />
            )}
            <button className="btn btn-danger" onClick={handleSignoutClick}>회원 탈퇴</button>
            {showSignout && (
                <SignoutModal
                    onSubmit={handleSignout}
                    onClose={() => setShowSignout(false)}
                />
            )}
            <button className="btn btn-primary" onClick={goToBoard}>리스트로 돌아가기</button>
        </div>
    );
};

export default UserProfile;
