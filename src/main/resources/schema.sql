-- Custom PostgreSQL view to expose user + profile data for read-optimized queries
CREATE OR REPLACE VIEW user_with_profile_view AS
SELECT
  u.id AS id,
  u.email AS email,
  p.full_name AS full_name,
  p.phone AS phone,
  p.address AS address,
  p.avatar AS avatar,
  u.role AS role,
  u.status AS status,
  u.created_at AS created_at,
  u.updated_at AS updated_at
FROM users u
JOIN user_profiles p ON p.user_id = u.id;


