export interface ProfileUpdateRequest {
    username: string;
    email: string;
    password?: string;
    newPassword?: string;
}