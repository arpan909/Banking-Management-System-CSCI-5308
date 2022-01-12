import { useContext, useState } from "react";
import { useHistory } from "react-router-dom";

//! Ant Imports

import Menu from "antd/lib/menu";
import Sider from "antd/lib/layout/Sider";

//! Ant Icons

import {
  AppstoreOutlined,
  MenuFoldOutlined,
  MenuUnfoldOutlined,
  UserOutlined,
} from "@ant-design/icons";

//! User Files

import { ROUTES } from "common/constants";
import { AppContext } from "AppContext";
import { rootSubMenuKeys, siderMenu } from "common/siderRoutes";

const { SubMenu } = Menu;

function AppSidebar() {
  const {
    state: { role },
  } = useContext(AppContext);
  const {
    push,
    location: { pathname },
  } = useHistory();
  const [openKeys, setOpenKeys] = useState([]);
  const [collapsed, setCollapsed] = useState(false);

  const toggle = () => {
    setCollapsed(!collapsed);
  };

  const onMenuSelect = (e) => {
    push(e.key);
  };

  const onOpenChange = (keys) => {
    const latestOpenKey = keys.find((key) => openKeys.indexOf(key) === -1);
    if (rootSubMenuKeys.indexOf(latestOpenKey) === -1) {
      setOpenKeys(keys);
    } else {
      setOpenKeys(latestOpenKey ? [latestOpenKey] : []);
    }
  };

  const renderSider = Object.keys(siderMenu).map((item) => {
    const hasSubMenu = siderMenu[item].views;
    return hasSubMenu
      ? siderMenu[item].allowedRoles.includes(role) && (
          <SubMenu key={item} icon={<UserOutlined />} title={item}>
            {siderMenu[item].views.map((subItem) => {
              return (
                subItem.allowedRoles.includes(role) && (
                  <Menu.Item key={subItem.link}>
                    <span>{subItem.label}</span>
                  </Menu.Item>
                )
              );
            })}
          </SubMenu>
        )
      : siderMenu[item].allowedRoles.includes(role) && (
          <Menu.Item key={siderMenu[item].link} icon={<AppstoreOutlined />}>
            <span>{siderMenu[item].label}</span>
          </Menu.Item>
        );
  });

  return (
    <Sider
      trigger={null}
      collapsible
      width={280}
      theme="light"
      collapsed={collapsed}
    >
      <div className="app-layout-sider-header">
        <div
          className={`${
            collapsed ? "app-icon-btn-collapsed" : "app-icon-btn-open"
          } app-icon-btn`}
          onClick={toggle}
        >
          {collapsed ? <MenuUnfoldOutlined /> : <MenuFoldOutlined />}
        </div>
      </div>
      <div className="app-sidebar-content">
        <Menu
          theme="lite"
          mode="inline"
          openKeys={openKeys}
          onOpenChange={onOpenChange}
          selectedKeys={[pathname]}
          defaultSelectedKeys={[ROUTES.USERS_MANAGEMENT]}
          onSelect={onMenuSelect}
        >
          {renderSider}
        </Menu>
      </div>
    </Sider>
  );
}

export default AppSidebar;
