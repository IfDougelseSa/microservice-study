-- Tabela Category
CREATE TABLE IF NOT EXISTS `category` (
    `id` bigint AUTO_INCREMENT PRIMARY KEY,
    `name` varchar(100) NOT NULL,
    `created_at` datetime NOT NULL,
    `created_by` varchar(50) NOT NULL,
    `updated_at` datetime DEFAULT NULL,
    `updated_by` varchar(50) DEFAULT NULL
    );

-- Tabela SubCategory
CREATE TABLE IF NOT EXISTS `subcategory` (
    `id` bigint AUTO_INCREMENT PRIMARY KEY,
    `name` varchar(100) NOT NULL,
    `category_id` bigint NOT NULL,
    `created_at` datetime NOT NULL,
    `created_by` varchar(50) NOT NULL,
    `updated_at` datetime DEFAULT NULL,
    `updated_by` varchar(50) DEFAULT NULL,
    FOREIGN KEY (`category_id`) REFERENCES `category`(`id`) ON DELETE CASCADE
    );

-- Tabela Content
CREATE TABLE IF NOT EXISTS `content` (
    `id` bigint AUTO_INCREMENT PRIMARY KEY,
    `title` varchar(200) NOT NULL,
    `text` text NOT NULL,
    `subcategory_id` bigint NOT NULL,
    `created_at` datetime NOT NULL,
    `created_by` varchar(50) NOT NULL,
    `updated_at` datetime DEFAULT NULL,
    `updated_by` varchar(50) DEFAULT NULL,
    FOREIGN KEY (`subcategory_id`) REFERENCES `subcategory`(`id`) ON DELETE CASCADE
    );