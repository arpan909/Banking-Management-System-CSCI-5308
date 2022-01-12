import { ROUTES } from "./constants";

export const rootSubMenuKeys = [
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

export const dashboardRoutes = [
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
  // {
  //   link: ROUTES.CHEQUE_SERVICES,
  //   label: "Cheque Services",
  //   allowedRoles: ["ROLE_USER"],
  // },
  {
    link: ROUTES.DEBIT_CARD_SERVICES,
    label: "Debit Card Services",
    allowedRoles: ["ROLE_USER"],
  },
  {
    link: ROUTES.CREDIT_CARD_SERVICES,
    label: "Credit Card Services",
    allowedRoles: ["ROLE_USER"],
  },
  {
    link: ROUTES.FD_SERVICES,
    label: "FD Services",
    allowedRoles: ["ROLE_USER"],
  },
  {
    link: ROUTES.FUND_TRANSFER,
    label: "Fund Transfer",
    allowedRoles: ["ROLE_USER"],
  },
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
  {
    link: ROUTES.LEAVE_REQUEST,
    label: "Leave Requests",
    allowedRoles: ["ROLE_MANAGER", "ROLE_HR"],
  },
  {
    link: ROUTES.RESIGNATION_REQUEST,
    label: "Resign Requests",
    allowedRoles: ["ROLE_MANAGER", "ROLE_HR"],
  },
  {
    link: ROUTES.APPLY_LEAVE,
    label: "Apply Leave",
    allowedRoles: ["ROLE_MANAGER", "ROLE_EMPLOYEE", "ROLE_HR"],
  },
  {
    link: ROUTES.APPLY_RESIGNATION,
    label: "Apply Resign",
    allowedRoles: ["ROLE_MANAGER", "ROLE_EMPLOYEE", "ROLE_HR"],
  },
  {
    link: ROUTES.MY_LEAVES,
    label: "My Leaves",
    allowedRoles: ["ROLE_MANAGER", "ROLE_EMPLOYEE", "ROLE_HR"],
  },
  {
    link: ROUTES.MY_RESIGN,
    label: "My Resign",
    allowedRoles: ["ROLE_MANAGER", "ROLE_EMPLOYEE", "ROLE_HR"],
  },
  {
    link: ROUTES.WITHDRAW,
    label: "Withdraw",
    allowedRoles: ["ROLE_MANAGER", "ROLE_EMPLOYEE"],
  },
  {
    link: ROUTES.DEPOSIT,
    label: "Deposit",
    allowedRoles: ["ROLE_MANAGER", "ROLE_EMPLOYEE"],
  },
];
