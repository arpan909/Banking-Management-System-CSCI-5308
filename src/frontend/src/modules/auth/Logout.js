import { useContext, useEffect } from "react";
import { useHistory } from "react-router-dom";

//! User Files

import * as ActionTypes from "common/actionTypes";
import { AppContext } from "AppContext";
import { ROUTES } from "common/constants";

const Logout = () => {
  const { dispatch } = useContext(AppContext);
  const { push } = useHistory();
  useEffect(() => {
    dispatch({ type: ActionTypes.LOGOUT });
    push(ROUTES.LOGIN);
    // eslint-disable-next-line
  }, []);

  return <div />;
};

export default Logout;
