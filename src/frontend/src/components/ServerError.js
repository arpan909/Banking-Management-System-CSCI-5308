import { useHistory } from "react-router-dom";

//! Ant Imports

import Result from "antd/lib/result";
import Button from "antd/lib/button";

//! User Files

import { ROUTES } from "common/constants";

function ServerError() {
  const { push } = useHistory();
  const handleRedirect = () => {
    push(ROUTES.MAIN);
  };
  return (
    <div className="sdp-error-wrapper">
      <Result
        status="500"
        title="500"
        subTitle="Sorry, something went wrong."
        extra={
          <Button type="primary" onClick={handleRedirect}>
            Back Home
          </Button>
        }
      />
    </div>
  );
}

export default ServerError;
