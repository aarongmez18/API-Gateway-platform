INSERT INTO API_KEY (X_CLIENT_ID, KEY_HASH, ACTIVE) VALUES

((SELECT X_CLIENT_ID FROM CLIENT WHERE NAME = 'Mobile App'),
 'hash_mobile_app_001', TRUE),

((SELECT X_CLIENT_ID FROM CLIENT WHERE NAME = 'Web App'),
 'hash_web_app_002', TRUE),

((SELECT X_CLIENT_ID FROM CLIENT WHERE NAME = 'Partner Empresa A'),
 'hash_partner_empresa_a_003', TRUE),

((SELECT X_CLIENT_ID FROM CLIENT WHERE NAME = 'Partner Empresa B'),
 'hash_partner_empresa_b_004', TRUE),

((SELECT X_CLIENT_ID FROM CLIENT WHERE NAME = 'Partner Empresa C'),
 'hash_partner_empresa_c_005', TRUE),

((SELECT X_CLIENT_ID FROM CLIENT WHERE NAME = 'Backoffice'),
 'hash_backoffice_006', TRUE),

((SELECT X_CLIENT_ID FROM CLIENT WHERE NAME = 'Admin Portal'),
 'hash_admin_portal_007', TRUE),

((SELECT X_CLIENT_ID FROM CLIENT WHERE NAME = 'Internal Service'),
 'hash_internal_service_008', TRUE),

((SELECT X_CLIENT_ID FROM CLIENT WHERE NAME = 'Reporting Tool'),
 'hash_reporting_tool_009', TRUE),

((SELECT X_CLIENT_ID FROM CLIENT WHERE NAME = 'Analytics Dashboard'),
 'hash_analytics_dashboard_010', TRUE),

((SELECT X_CLIENT_ID FROM CLIENT WHERE NAME = 'Customer Portal'),
 'hash_customer_portal_011', TRUE),

((SELECT X_CLIENT_ID FROM CLIENT WHERE NAME = 'Supplier Portal'),
 'hash_supplier_portal_012', TRUE),

((SELECT X_CLIENT_ID FROM CLIENT WHERE NAME = 'Billing Service'),
 'hash_billing_service_013', TRUE),

((SELECT X_CLIENT_ID FROM CLIENT WHERE NAME = 'Notification Service'),
 'hash_notification_service_014', TRUE),

((SELECT X_CLIENT_ID FROM CLIENT WHERE NAME = 'Orders Service'),
 'hash_orders_service_015', TRUE),

((SELECT X_CLIENT_ID FROM CLIENT WHERE NAME = 'Payments Service'),
 'hash_payments_service_016', TRUE),

((SELECT X_CLIENT_ID FROM CLIENT WHERE NAME = 'Invoices Service'),
 'hash_invoices_service_017', TRUE),

((SELECT X_CLIENT_ID FROM CLIENT WHERE NAME = 'Shipping Service'),
 'hash_shipping_service_018', TRUE),

((SELECT X_CLIENT_ID FROM CLIENT WHERE NAME = 'Inventory Service'),
 'hash_inventory_service_019', TRUE),

((SELECT X_CLIENT_ID FROM CLIENT WHERE NAME = 'Catalog Service'),
 'hash_catalog_service_020', TRUE),

((SELECT X_CLIENT_ID FROM CLIENT WHERE NAME = 'Support Tool'),
 'hash_support_tool_021', TRUE),

((SELECT X_CLIENT_ID FROM CLIENT WHERE NAME = 'Monitoring Tool'),
 'hash_monitoring_tool_022', TRUE),

((SELECT X_CLIENT_ID FROM CLIENT WHERE NAME = 'Audit Tool'),
 'hash_audit_tool_023', TRUE),

((SELECT X_CLIENT_ID FROM CLIENT WHERE NAME = 'Logs Service'),
 'hash_logs_service_024', TRUE),

((SELECT X_CLIENT_ID FROM CLIENT WHERE NAME = 'Metrics Service'),
 'hash_metrics_service_025', TRUE),

((SELECT X_CLIENT_ID FROM CLIENT WHERE NAME = 'Search Service'),
 'hash_search_service_026', TRUE),

((SELECT X_CLIENT_ID FROM CLIENT WHERE NAME = 'Files Service'),
 'hash_files_service_027', TRUE),

((SELECT X_CLIENT_ID FROM CLIENT WHERE NAME = 'Documents Service'),
 'hash_documents_service_028', TRUE),

((SELECT X_CLIENT_ID FROM CLIENT WHERE NAME = 'Images Service'),
 'hash_images_service_029', TRUE),

((SELECT X_CLIENT_ID FROM CLIENT WHERE NAME = 'Chat Service'),
 'hash_chat_service_030', TRUE),

((SELECT X_CLIENT_ID FROM CLIENT WHERE NAME = 'Mobile App QA'),
 'hash_mobile_qa_031', TRUE),

((SELECT X_CLIENT_ID FROM CLIENT WHERE NAME = 'Web App QA'),
 'hash_web_qa_032', TRUE),

((SELECT X_CLIENT_ID FROM CLIENT WHERE NAME = 'Partner Sandbox A'),
 'hash_partner_sandbox_a_033', TRUE),

((SELECT X_CLIENT_ID FROM CLIENT WHERE NAME = 'Partner Sandbox B'),
 'hash_partner_sandbox_b_034', TRUE),

((SELECT X_CLIENT_ID FROM CLIENT WHERE NAME = 'Development Client'),
 'hash_development_035', TRUE),

((SELECT X_CLIENT_ID FROM CLIENT WHERE NAME = 'Testing Client'),
 'hash_testing_036', TRUE),

((SELECT X_CLIENT_ID FROM CLIENT WHERE NAME = 'Postman Client'),
 'hash_postman_037', TRUE),

((SELECT X_CLIENT_ID FROM CLIENT WHERE NAME = 'Swagger Client'),
 'hash_swagger_038', TRUE),

((SELECT X_CLIENT_ID FROM CLIENT WHERE NAME = 'Local Client'),
 'hash_local_039', TRUE),

((SELECT X_CLIENT_ID FROM CLIENT WHERE NAME = 'Integration Client'),
 'hash_integration_040', TRUE),

((SELECT X_CLIENT_ID FROM CLIENT WHERE NAME = 'Legacy Client A'),
 'hash_legacy_a_041', FALSE),

((SELECT X_CLIENT_ID FROM CLIENT WHERE NAME = 'Legacy Client B'),
 'hash_legacy_b_042', FALSE),

((SELECT X_CLIENT_ID FROM CLIENT WHERE NAME = 'Old Mobile App'),
 'hash_old_mobile_043', FALSE),

((SELECT X_CLIENT_ID FROM CLIENT WHERE NAME = 'Old Web App'),
 'hash_old_web_044', FALSE),

((SELECT X_CLIENT_ID FROM CLIENT WHERE NAME = 'Deprecated Partner'),
 'hash_deprecated_partner_045', FALSE),

((SELECT X_CLIENT_ID FROM CLIENT WHERE NAME = 'Blocked Client A'),
 'hash_blocked_a_046', FALSE),

((SELECT X_CLIENT_ID FROM CLIENT WHERE NAME = 'Blocked Client B'),
 'hash_blocked_b_047', FALSE),

((SELECT X_CLIENT_ID FROM CLIENT WHERE NAME = 'Suspended Client'),
 'hash_suspended_048', FALSE),

((SELECT X_CLIENT_ID FROM CLIENT WHERE NAME = 'Expired Client'),
 'hash_expired_049', FALSE),

((SELECT X_CLIENT_ID FROM CLIENT WHERE NAME = 'Disabled Test Client'),
 'hash_disabled_test_050', FALSE);