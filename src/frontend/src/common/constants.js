/* ROUTERS  */
export const ROUTES = {
  /* Common */
  MAIN: "/",
  LOGIN: "/login",
  LOGOUT: "/logout",
  REGISTER: "/register",
  FORGET_PASSWORD: "/forget-password",
  CHANGE_PASSWORD: "/change-password",
  USERS_MANAGEMENT: "/users",
  RESET_PASSWORD: "/reset-password",
  PROFILE: "/profile",
  UPDATE_PROFILE: "/profile/update",
  /* Customer */
  MY_ACCOUNT: "/my-account",
  ACCOUNT_STATEMENT: "/account-statement",
  CHEQUE_SERVICES: "/service/cheque",
  DEBIT_CARD_SERVICES: "/service/debit-card",
  CREDIT_CARD_SERVICES: "/service/credit-card",
  FD_SERVICES: "/service/fd",
  FUND_TRANSFER: "/service/fund-transfer",
  /* Bank */
  ADD_USER: "/bank/user/create",
  ACCOUNT_OPENING_REQUEST: "/bank/customer/requests",
  CREDIT_CARD_REQUEST: "/bank/credit-card/requests",
  APPLY_LEAVE: "/bank/leave/apply",
  LEAVE_REQUEST: "/bank/leave/requests",
  APPLY_RESIGNATION: "/bank/resign/apply",
  RESIGNATION_REQUEST: "/bank/resign/requests",
  MY_LEAVES: "/my-leaves",
  MY_RESIGN: "/my-resign",
  WITHDRAW: "/bank/withdraw",
  DEPOSIT: "/bank/deposit",
};

export const APP_NAME = "DAL BMS";

export const MINIMUM_CREDIT_SCORE = 650;

export const ACCOUNT_STATUS = {
  PENDING: "PENDING",
  ACTIVE: "ACTIVE",
  REJECTED: "REJECTED",
  CLOSED: "CLOSED",
};

export const CREDIT_CARD_STATUS = {
  PENDING: "PENDING",
  APPROVED: "APPROVED",
  DECLINED: "DECLINED",
};

export const LEAVE_STATUS = {
  PENDING: "PENDING",
  APPROVED: "APPROVED",
  REJECTED: "REJECTED",
};

export const RESIGN_STATUS = {
  PENDING: "PENDING",
  APPROVED: "APPROVED",
  REJECTED: "REJECTED",
};

/*  Modules */
export const MODULES = {
  DASHBOARD: "Dashboard",
  USERS_MANAGEMENT: "User Management",
  PROFILE: "My Profile",
  UPDATE_PROFILE: "Update Profile",
  /* Bank */
  ADD_USER: "Add User",
  ACCOUNT_OPENING_REQUEST: "Account Opening Requests",
  CREDIT_CARD_REQUEST: "Credit Card Requests",
  APPLY_LEAVE: "Apply Leave",
  LEAVE_REQUEST: "Leave Requests",
  APPLY_RESIGNATION: "Apply Resignation",
  RESIGNATION_REQUEST: "Resignation Requests",
  /* Customer */
  MY_ACCOUNT: "My Account",
  ACCOUNT_STATEMENT: "Account Statement",
  CHEQUE_SERVICES: "Cheque Services",
  DEBIT_CARD_SERVICES: "Debit Card Services",
  CREDIT_CARD_SERVICES: "Credit Card Services",
  FD_SERVICES: "FD Services",
  WITHDRAW: "Withdraw",
  DEPOSIT: "Deposit",
};

/* Authentication */
export const TOKEN = "TOKEN";
export const USER = "USER";
export const ADMIN = "ADMIN";
export const USER_ID = "USER_ID";
export const ROLE = "ROLE";

/* Errors */

export const SERVER_ERROR = "SERVER_ERROR";

export const ROLES = {
  ROLE_MANAGER: "ROLE_MANAGER",
  ROLE_HR: "ROLE_HR",
  ROLE_EMPLOYEE: "ROLE_EMPLOYEE",
  ROLE_USER: "ROLE_USER",
};

/* Date and time */
export const defaultDateFormat = "MM/DD/YYYY";

export const REGEX = {
  NAME: /^[a-z ,.'-]+$/i,
  ZIPCODE: /^[0-9]{5,6}$/,
  CITY: /^[a-zA-Z]+(?:[\s-][a-zA-Z]+)*$/,
  WEB_URL:
    /(https?:\/\/(?:www\.|(?!www))[a-zA-Z0-9][a-zA-Z0-9-]+[a-zA-Z0-9]\.[^\s]{2,}|www\.[a-zA-Z0-9][a-zA-Z0-9-]+[a-zA-Z0-9]\.[^\s]{2,}|https?:\/\/(?:www\.|(?!www))[a-zA-Z0-9]+\.[^\s]{2,}|www\.[a-zA-Z0-9]+\.[^\s]{2,})/gi,
  PASSWORD: /^(?=.*[0-9])(?=.*[a-zA-Z])[a-zA-Z0-9!@#$%^&*]{8,16}$/,
  PHONE: /^[+]?[(]?[0-9]{3}[)]?[-\s.]?[0-9]{3}[-\s.]?[0-9]{4,6}$/,
  EMAIL: /^[a-z0-9.]+@[a-zA-Z0-9-]+\.[a-zA-Z0-9-.]+$/,
  AMOUNT: /^\d+$|^\d+\.\d*$/,
  OPTIONALNEGATIVEAMOUNT: /^[-]?\d+$|^[-]?\d+\.\d*$/,
  NUMBER: /^\d+$/,
};
