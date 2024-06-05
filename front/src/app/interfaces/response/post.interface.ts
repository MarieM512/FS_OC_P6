import { Topic } from "./topic.interface";
import { User } from "./user.interface";

export interface Post {
    id: number;
    topic: Topic;
    subject: string;
    content: string;
    user: User;
    createdAt: Date;
}