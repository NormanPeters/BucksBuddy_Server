// src/types.ts

export interface User {
    id: number;
    email?: string;
    password?: string;
    journeys: Journey[];
    uuid: string;
    isEditing?: boolean;
}

export interface newUser {
    email: string;
    password: string;
}

export interface Journey {
    id: number;
    name: string;
    homeCurr: string;
    vacCurr: string;
    budget: number;
    startDate: Date;
    endDate: Date;
    travelDuration: string;
    isEditing?: boolean;
}

export interface Expenditure {
    id: number;
    name: string;
    amount: number;
    date: Date;
    journeyId: number;
    isEditing?: boolean;
}
