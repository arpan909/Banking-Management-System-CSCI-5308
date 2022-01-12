import { ROUTES } from "./constants";

export const rootSubMenuKeys = [
  "Dashboard",
  "Profile",
  "Account",
  "Cheque Services",
  "Debit Card Services",
  "Credit Card Services",
  "FD Services",
  "User Management",
  "Leave",
  "Resign",
];

export const siderMenu = {
  Dashboard: {
    link: ROUTES.MAIN,
    label: "Dashboard",
    allowedRoles: ["ROLE_MANAGER", "ROLE_EMPLOYEE", "ROLE_HR", "ROLE_USER"],
  },
  Profile: {
    allowedRoles: ["ROLE_MANAGER", "ROLE_EMPLOYEE", "ROLE_HR", "ROLE_USER"],
    views: [
      {
        link: ROUTES.PROFILE,
        label: "My Profile",
        allowedRoles: ["ROLE_MANAGER", "ROLE_EMPLOYEE", "ROLE_HR", "ROLE_USER"],
      },
      {
        link: ROUTES.UPDATE_PROFILE,
        label: "Update Profile",
        allowedRoles: ["ROLE_MANAGER", "ROLE_EMPLOYEE", "ROLE_HR", "ROLE_USER"],
      },
    ],
  },
  Account: {
    allowedRoles: ["ROLE_USER"],
    views: [
      {
        link: ROUTES.MY_ACCOUNT,
        label: "My Account",
        allowedRoles: ["ROLE_USER"],
      },
      {
        link: ROUTES.ACCOUNT_STATEMENT,
        label: "Account Statement",
        allowedRoles: ["ROLE_USER"],
      },
    ],
  },
  // "Cheque Services": {
  //   link: ROUTES.CHEQUE_SERVICES,
  //   label: "Cheque Services",
  //   allowedRoles: ["ROLE_USER"],
  // },
  "Debit Card Services": {
    link: ROUTES.DEBIT_CARD_SERVICES,
    label: "Debit Card Services",
    allowedRoles: ["ROLE_USER"],
  },
  "Credit Card Services": {
    link: ROUTES.CREDIT_CARD_SERVICES,
    label: "Credit Card Services",
    allowedRoles: ["ROLE_USER"],
  },
  "FD Services": {
    link: ROUTES.FD_SERVICES,
    label: "FD Services",
    allowedRoles: ["ROLE_USER"],
  },
  "Fund Transfer": {
    link: ROUTES.FUND_TRANSFER,
    label: "Fund Transfer",
    allowedRoles: ["ROLE_USER"],
  },
  Withdraw: {
    link: ROUTES.WITHDRAW,
    label: "Withdraw",
    allowedRoles: ["ROLE_MANAGER", "ROLE_EMPLOYEE"],
  },
  Deposit: {
    link: ROUTES.DEPOSIT,
    label: "Deposit",
    allowedRoles: ["ROLE_MANAGER", "ROLE_EMPLOYEE"],
  },
  "User Management": {
    allowedRoles: ["ROLE_MANAGER", "ROLE_EMPLOYEE", "ROLE_HR"],
    views: [
      {
        link: ROUTES.ADD_USER,
        label: "Add User",
        allowedRoles: ["ROLE_MANAGER", "ROLE_HR"],
      },
      {
        link: ROUTES.ACCOUNT_OPENING_REQUEST,
        label: "Account Opening Requests",
        allowedRoles: ["ROLE_MANAGER", "ROLE_EMPLOYEE"],
      },
      {
        link: ROUTES.CREDIT_CARD_REQUEST,
        label: "Credit Card Requests",
        allowedRoles: ["ROLE_MANAGER", "ROLE_EMPLOYEE"],
      },
    ],
  },
  Leave: {
    allowedRoles: ["ROLE_MANAGER", "ROLE_EMPLOYEE", "ROLE_HR"],
    views: [
      {
        link: ROUTES.APPLY_LEAVE,
        label: "Apply Leave",
        allowedRoles: ["ROLE_MANAGER", "ROLE_EMPLOYEE", "ROLE_HR"],
      },
      {
        link: ROUTES.LEAVE_REQUEST,
        label: "Leave Requests",
        allowedRoles: ["ROLE_MANAGER", "ROLE_HR"],
      },
    ],
  },
  Resign: {
    allowedRoles: ["ROLE_MANAGER", "ROLE_EMPLOYEE", "ROLE_HR"],
    views: [
      {
        link: ROUTES.APPLY_RESIGNATION,
        label: "Apply Resignation",
        allowedRoles: ["ROLE_MANAGER", "ROLE_EMPLOYEE", "ROLE_HR"],
      },
      {
        link: ROUTES.RESIGNATION_REQUEST,
        label: "Resign Requests",
        allowedRoles: ["ROLE_MANAGER", "ROLE_HR"],
      },
    ],
  },
};
