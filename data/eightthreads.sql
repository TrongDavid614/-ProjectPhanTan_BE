CREATE TABLE Users (
                       user_id VARCHAR(50) PRIMARY KEY,
                       first_name VARCHAR(100),
                       last_name VARCHAR(100),
                       email VARCHAR(255) UNIQUE NOT NULL,
                       role VARCHAR(20) DEFAULT 'customer',
                       status VARCHAR(20) DEFAULT 'active',
                       auth_provider VARCHAR(20) DEFAULT 'local',
                       provider_id VARCHAR(255),
                       password VARCHAR(255),
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                       updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE Event (
                       event_id VARCHAR(50) PRIMARY KEY,
                       name VARCHAR(255) NOT NULL,
                       category_id VARCHAR(50),
                       description TEXT,
                       img VARCHAR(500),
                       start_time DATETIME,
                       end_time DATETIME,
                       sale_start DATETIME,
                       sale_end DATETIME,
                       venue_name VARCHAR(255),
                       city VARCHAR(100),
                       country VARCHAR(100),
                       status VARCHAR(20) DEFAULT 'active',
                       created_by VARCHAR(50),
                       CONSTRAINT fk_event_creator
                           FOREIGN KEY (created_by) REFERENCES Users(user_id)
);

CREATE TABLE Ticket_Types (
                              ticket_type_id VARCHAR(50) PRIMARY KEY,
                              event_id VARCHAR(50) NOT NULL,
                              name VARCHAR(100) NOT NULL,
                              type VARCHAR(50),
                              price DECIMAL(15,2) NOT NULL DEFAULT 0,
                              total_quantity INT NOT NULL DEFAULT 0,
                              sold_quantity INT NOT NULL DEFAULT 0,
                              is_active BOOLEAN DEFAULT TRUE,
                              created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

                              CONSTRAINT fk_ticket_type_event
                                  FOREIGN KEY (event_id) REFERENCES Event(event_id)
                                      ON DELETE CASCADE
);

CREATE TABLE Orders (
                        order_id VARCHAR(50) PRIMARY KEY,
                        user_id VARCHAR(50) NOT NULL,
                        event_id VARCHAR(50) NOT NULL,
                        total_amount DECIMAL(15,2) NOT NULL DEFAULT 0,
                        status VARCHAR(20) DEFAULT 'pending',
                        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

                        CONSTRAINT fk_orders_user
                            FOREIGN KEY (user_id) REFERENCES Users(user_id),

                        CONSTRAINT fk_orders_event
                            FOREIGN KEY (event_id) REFERENCES Event(event_id)
);

CREATE TABLE Vouchers (
                          voucher_id VARCHAR(50) PRIMARY KEY,
                          voucher_name VARCHAR(255) NOT NULL,
                          conditions TEXT,
                          time_start DATETIME,
                          time_end DATETIME,
                          promotion VARCHAR(255),
                          voucher_type ENUM('percent', 'fixed') NOT NULL,
                          value DECIMAL(15,2) NOT NULL,
                          created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE Voucher_Events (
                                voucher_id VARCHAR(50),
                                event_id VARCHAR(50),

                                PRIMARY KEY (voucher_id, event_id),

                                CONSTRAINT fk_ve_voucher
                                    FOREIGN KEY (voucher_id) REFERENCES Vouchers(voucher_id)
                                        ON DELETE CASCADE,

                                CONSTRAINT fk_ve_event
                                    FOREIGN KEY (event_id) REFERENCES Event(event_id)
                                        ON DELETE CASCADE
);

CREATE TABLE Order_Items (
                             id INT AUTO_INCREMENT PRIMARY KEY,
                             order_id VARCHAR(50) NOT NULL,
                             ticket_type_id VARCHAR(50) NOT NULL,
                             quantity INT NOT NULL DEFAULT 1,
                             unit_price DECIMAL(15,2) NOT NULL,
                             subtotal DECIMAL(15,2) NOT NULL,
                             voucher_id VARCHAR(50),

                             CONSTRAINT fk_order_items_order
                                 FOREIGN KEY (order_id) REFERENCES Orders(order_id)
                                     ON DELETE CASCADE,

                             CONSTRAINT fk_order_items_type
                                 FOREIGN KEY (ticket_type_id) REFERENCES Ticket_Types(ticket_type_id),

                             CONSTRAINT fk_order_items_voucher
                                 FOREIGN KEY (voucher_id) REFERENCES Vouchers(voucher_id)
);

CREATE TABLE Payments (
                          payment_id VARCHAR(50) PRIMARY KEY,
                          order_id VARCHAR(50) UNIQUE NOT NULL,
                          method VARCHAR(20) DEFAULT 'vnpay',
                          amount DECIMAL(15,2) NOT NULL,
                          currency VARCHAR(10) DEFAULT 'VND',
                          status VARCHAR(20) DEFAULT 'pending',
                          txn_ref VARCHAR(100),
                          paid_at DATETIME,

                          CONSTRAINT fk_payments_order
                              FOREIGN KEY (order_id) REFERENCES Orders(order_id)
);

CREATE TABLE Tickets (
                         ticket_id VARCHAR(50) PRIMARY KEY,
                         event_id VARCHAR(50) NOT NULL,
                         ticket_type_id VARCHAR(50) NOT NULL,
                         order_id VARCHAR(50) NOT NULL,
                         owner_id VARCHAR(50) NOT NULL,
                         qr_code VARCHAR(255) UNIQUE NOT NULL,
                         status VARCHAR(20) DEFAULT 'valid',
                         created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

                         CONSTRAINT fk_tickets_event
                             FOREIGN KEY (event_id) REFERENCES Event(event_id),

                         CONSTRAINT fk_tickets_type
                             FOREIGN KEY (ticket_type_id) REFERENCES Ticket_Types(ticket_type_id),

                         CONSTRAINT fk_tickets_order
                             FOREIGN KEY (order_id) REFERENCES Orders(order_id),

                         CONSTRAINT fk_tickets_owner
                             FOREIGN KEY (owner_id) REFERENCES Users(user_id)
);