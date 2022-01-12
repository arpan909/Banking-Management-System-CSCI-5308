import Error404 from "Error404";
import Account from "modules/account";
import AccountStatement from "modules/account/components/AccountStatement";
import ApplyLeave from "modules/admin/leave/ApplyLeave";
import MyLeaves from "modules/admin/leave/components/MyLeaves";
import LeaveRequests from "modules/admin/leave/LeaveRequests";
import ApplyResignation from "modules/admin/resign/ApplyResignation";
import MyResign from "modules/admin/resign/components/MyResign";
import ResignationRequests from "modules/admin/resign/ResignationRequests";
import AccountOpeningRequests from "modules/admin/user_management/AccountOpeningRequests";
import AddUser from "modules/admin/user_management/AddUser";
import CreditCardRequests from "modules/admin/user_management/CreditCardRequests";
import Deposit from "modules/admin/user_management/Deposit";
import Withdraw from "modules/admin/user_management/Withdraw";
import ChangePassword from "modules/auth/components/ChangePassword";
import ChequeServices from "modules/cheque_service";
import CreditCardServices from "modules/credit_card_service";
import Dashboard from "modules/dashboard";
import DebitCardServices from "modules/debit_card_service";
import FDServices from "modules/fd_service";
import FundTransfer from "modules/fund_transfer";
import Profile from "modules/profile";
import UpdateProfile from "modules/profile/components/UpdateProfile";
import { ROUTES } from "./constants";

export const routesList = [
  {
    link: ROUTES.MAIN,
    label: "Dashboard",
    view: Dashboard,
    allowedRoles: ["ROLE_MANAGER", "ROLE_EMPLOYEE", "ROLE_HR", "ROLE_USER"],
  },
  {
    link: ROUTES.CHANGE_PASSWORD,
    label: "Change Password",
    view: ChangePassword,
    allowedRoles: ["ROLE_MANAGER", "ROLE_EMPLOYEE", "ROLE_HR", "ROLE_USER"],
  },
  {
    link: ROUTES.PROFILE,
    label: "My Profile",
    view: Profile,
    allowedRoles: ["ROLE_MANAGER", "ROLE_EMPLOYEE", "ROLE_HR", "ROLE_USER"],
  },
  {
    link: ROUTES.UPDATE_PROFILE,
    label: "Update Profile",
    view: UpdateProfile,
    allowedRoles: ["ROLE_MANAGER", "ROLE_EMPLOYEE", "ROLE_HR", "ROLE_USER"],
  },
  {
    link: ROUTES.MY_ACCOUNT,
    label: "My Account",
    view: Account,
    allowedRoles: ["ROLE_USER"],
  },
  {
    link: ROUTES.ACCOUNT_STATEMENT,
    label: "Account Statement",
    view: AccountStatement,
    allowedRoles: ["ROLE_USER"],
  },
  {
    link: ROUTES.CHEQUE_SERVICES,
    label: "Cheque Services",
    view: ChequeServices,
    allowedRoles: ["ROLE_USER"],
  },
  {
    link: ROUTES.DEBIT_CARD_SERVICES,
    label: "Debit Card Services",
    view: DebitCardServices,
    allowedRoles: ["ROLE_USER"],
  },
  {
    link: ROUTES.CREDIT_CARD_SERVICES,
    label: "Credit Card Services",
    view: CreditCardServices,
    allowedRoles: ["ROLE_USER"],
  },
  {
    link: ROUTES.FD_SERVICES,
    label: "FD Services",
    view: FDServices,
    allowedRoles: ["ROLE_USER"],
  },
  {
    link: ROUTES.FUND_TRANSFER,
    label: "Fund Transfer",
    view: FundTransfer,
    allowedRoles: ["ROLE_USER"],
  },
  {
    link: ROUTES.ADD_USER,
    label: "Add User",
    view: AddUser,
    allowedRoles: ["ROLE_MANAGER", "ROLE_HR"],
  },
  {
    link: ROUTES.ACCOUNT_OPENING_REQUEST,
    label: "Account Opening Requests",
    view: AccountOpeningRequests,
    allowedRoles: ["ROLE_MANAGER", "ROLE_EMPLOYEE"],
  },
  {
    link: ROUTES.CREDIT_CARD_REQUEST,
    label: "Credit Card Requests",
    view: CreditCardRequests,
    allowedRoles: ["ROLE_MANAGER", "ROLE_EMPLOYEE"],
  },
  {
    link: ROUTES.APPLY_LEAVE,
    label: "Apply Leave",
    view: ApplyLeave,
    allowedRoles: ["ROLE_MANAGER", "ROLE_EMPLOYEE", "ROLE_HR"],
  },
  {
    link: ROUTES.LEAVE_REQUEST,
    label: "Leave Requests",
    view: LeaveRequests,
    allowedRoles: ["ROLE_MANAGER", "ROLE_HR"],
  },
  {
    link: ROUTES.APPLY_RESIGNATION,
    label: "Apply Resignation",
    view: ApplyResignation,
    allowedRoles: ["ROLE_MANAGER", "ROLE_EMPLOYEE", "ROLE_HR"],
  },
  {
    link: ROUTES.RESIGNATION_REQUEST,
    label: "Resign Requests",
    view: ResignationRequests,
    allowedRoles: ["ROLE_MANAGER", "ROLE_HR"],
  },
  {
    link: ROUTES.MY_LEAVES,
    label: "My Leaves",
    view: MyLeaves,
    allowedRoles: ["ROLE_MANAGER", "ROLE_EMPLOYEE", "ROLE_HR"],
  },
  {
    link: ROUTES.MY_RESIGN,
    label: "My Resign",
    view: MyResign,
    allowedRoles: ["ROLE_MANAGER", "ROLE_EMPLOYEE", "ROLE_HR"],
  },
  {
    link: ROUTES.WITHDRAW,
    label: "Withdraw",
    view: Withdraw,
    allowedRoles: ["ROLE_MANAGER", "ROLE_EMPLOYEE"],
  },
  {
    link: ROUTES.DEPOSIT,
    label: "Deposit",
    view: Deposit,
    allowedRoles: ["ROLE_MANAGER", "ROLE_EMPLOYEE"],
  },
  {
    link: "*",
    label: "Error",
    view: Error404,
    allowedRoles: ["ROLE_MANAGER", "ROLE_EMPLOYEE", "ROLE_HR", "ROLE_USER"],
  },
];
