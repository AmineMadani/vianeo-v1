/*
  # Fix Personnel RLS Policies

  1. Security Updates
    - Drop existing restrictive policies on personnel table
    - Add new policies allowing authenticated users to manage personnel data
    - Ensure INSERT, UPDATE, DELETE operations work for authenticated users

  2. Changes
    - Remove overly restrictive policies
    - Add comprehensive CRUD policies for authenticated role
*/

-- Drop existing policies that might be too restrictive
DROP POLICY IF EXISTS "Users can manage personnel" ON personnel;
DROP POLICY IF EXISTS "Users can read all personnel" ON personnel;

-- Create new policies for authenticated users
CREATE POLICY "Authenticated users can read personnel"
  ON personnel
  FOR SELECT
  TO authenticated
  USING (true);

CREATE POLICY "Authenticated users can insert personnel"
  ON personnel
  FOR INSERT
  TO authenticated
  WITH CHECK (true);

CREATE POLICY "Authenticated users can update personnel"
  ON personnel
  FOR UPDATE
  TO authenticated
  USING (true)
  WITH CHECK (true);

CREATE POLICY "Authenticated users can delete personnel"
  ON personnel
  FOR DELETE
  TO authenticated
  USING (true);