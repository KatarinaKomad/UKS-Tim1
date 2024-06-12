import { UserDTO } from "../user/user";

export interface Comment {
  id: number;
  message: string;
  createdAt: Date;
  author: UserDTO;
  itemId: string;
}

export interface CommentRequest {
  message: string;
}
