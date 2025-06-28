-- Extensions de base uniquement
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Table des organisations
CREATE TABLE IF NOT EXISTS organization (
    organization_id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    org_name TEXT NOT NULL,
    org_code TEXT UNIQUE,
    org_type TEXT,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

-- Table des utilisateurs
CREATE TABLE IF NOT EXISTS app_user (
    user_id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    organization_id UUID NOT NULL REFERENCES organization(organization_id) ON DELETE CASCADE,
    username TEXT NOT NULL,
    email TEXT,
    phone TEXT,
    password_hash TEXT,
    role TEXT NOT NULL,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

-- Table POI - Version R2DBC compatible
CREATE TABLE IF NOT EXISTS point_of_interest (
    poi_id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    organization_id UUID REFERENCES organization(organization_id),
    town_id UUID,
    created_by_user_id UUID REFERENCES app_user(user_id),

    poi_name TEXT NOT NULL,
    poi_type TEXT NOT NULL,
    poi_category TEXT NOT NULL,
    poi_long_name TEXT,
    poi_short_name TEXT,
    poi_friendly_name TEXT,
    poi_description TEXT,

    poi_logo BYTEA,
    
    -- Coordonnées GPS simples (au lieu de GEOGRAPHY)
    latitude DECIMAL(10, 8),
    longitude DECIMAL(11, 8),

    -- Adresse décomposée (au lieu du type composite)
    address_street_number TEXT,
    address_street_name TEXT,
    address_city TEXT,
    address_state_province TEXT,
    address_postal_code TEXT,
    address_country TEXT,
    address_informal TEXT,

    phone_number TEXT,
    website_url TEXT,

    -- JSON au lieu de JSONB pour meilleure compatibilité
    operation_time_plan JSON,
    
    -- Contacts comme JSON au lieu de type composite
    poi_contacts JSON,
    
    -- Tableaux convertis en TEXT avec délimiteurs
    poi_images_urls TEXT, -- URLs séparées par virgules
    poi_amenities TEXT,   -- Amenities séparés par virgules  
    poi_keywords TEXT,    -- Keywords séparés par virgules
    poi_type_tags TEXT,   -- Tags séparés par virgules

    popularity_score FLOAT DEFAULT 0,

    is_active BOOLEAN DEFAULT TRUE,
    deactivation_reason TEXT,
    deactivated_by_user_id UUID REFERENCES app_user(user_id),

    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    updated_by_user_id UUID REFERENCES app_user(user_id),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

-- Table des logs d'accès
CREATE TABLE IF NOT EXISTS poi_access_log (
    access_id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    poi_id UUID NOT NULL REFERENCES point_of_interest(poi_id) ON DELETE CASCADE,
    organization_id UUID NOT NULL REFERENCES organization(organization_id) ON DELETE CASCADE,
    platform_type TEXT NOT NULL,
    user_id UUID REFERENCES app_user(user_id),
    access_type TEXT,
    access_datetime TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    -- Métadonnées comme JSON simple
    metadata JSON
);

-- Table des reviews
CREATE TABLE IF NOT EXISTS poi_review (
    review_id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    poi_id UUID NOT NULL REFERENCES point_of_interest(poi_id) ON DELETE CASCADE,
    user_id UUID NOT NULL REFERENCES app_user(user_id),
    organization_id UUID NOT NULL REFERENCES organization(organization_id) ON DELETE CASCADE,
    platform_type TEXT NOT NULL,
    rating INT CHECK (rating >= 1 AND rating <= 5),
    review_text TEXT,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    likes INT DEFAULT 0,
    dislikes INT DEFAULT 0
);

-- Table des statistiques
CREATE TABLE IF NOT EXISTS poi_platform_stat (
    stat_id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    org_id UUID NOT NULL REFERENCES organization(organization_id) ON DELETE CASCADE,
    poi_id UUID REFERENCES point_of_interest(poi_id) ON DELETE CASCADE,
    platform_type TEXT NOT NULL,
    stat_date DATE NOT NULL,
    views INT DEFAULT 0,
    reviews INT DEFAULT 0,
    likes INT DEFAULT 0,
    dislikes INT DEFAULT 0
);

-- Index pour performance
CREATE INDEX IF NOT EXISTS idx_poi_org_id ON point_of_interest (organization_id);
CREATE INDEX IF NOT EXISTS idx_poi_type ON point_of_interest (poi_type);
CREATE INDEX IF NOT EXISTS idx_poi_category ON point_of_interest (poi_category);
CREATE INDEX IF NOT EXISTS idx_poi_name ON point_of_interest (poi_name);
CREATE INDEX IF NOT EXISTS idx_poi_location ON point_of_interest (latitude, longitude);
CREATE INDEX IF NOT EXISTS idx_poi_is_active ON point_of_interest (is_active);

CREATE INDEX IF NOT EXISTS idx_access_log_poi ON poi_access_log (poi_id);
CREATE INDEX IF NOT EXISTS idx_access_log_org ON poi_access_log (organization_id);
CREATE INDEX IF NOT EXISTS idx_access_log_platform ON poi_access_log (platform_type);
CREATE INDEX IF NOT EXISTS idx_access_log_date ON poi_access_log (access_datetime);

CREATE INDEX IF NOT EXISTS idx_poi_review_poi_id ON poi_review (poi_id);
CREATE INDEX IF NOT EXISTS idx_poi_review_org ON poi_review (organization_id);
CREATE INDEX IF NOT EXISTS idx_stat_org_platform ON poi_platform_stat (org_id, platform_type, stat_date);