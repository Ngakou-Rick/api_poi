import requests
import json
import uuid
import time

BASE_URL = "http://localhost:8080/api"

def test_crud_organizations():
    print("\n--- Testing Organizations CRUD ---")

    # 1. Create
    org_data = {
        "organizationName": "Test Org " + str(uuid.uuid4())[:8],
        "orgCode": "ORG" + str(uuid.uuid4())[:4].upper(),
        "orgType": "MERCHANT",
        "isActive": True
    }
    response = requests.post(f"{BASE_URL}/organizations", json=org_data)
    print(f"CREATE ORG: Status {response.status_code}")
    if response.status_code not in [200, 201]:
        print(f"Error: {response.text}")
        return None
    org = response.json()
    org_id = org['organizationId']
    print(f"Created Org ID: {org_id}")
    return org_id

def test_crud_users(organization_id):
    print("\n--- Testing Users CRUD ---")

    # 1. Create
    user_data = {
        "organizationId": organization_id,
        "username": "user_" + str(uuid.uuid4())[:8],
        "email": "user_" + str(uuid.uuid4())[:8] + "@example.com",
        "password": "Password123!",
        "role": "USER",
        "isActive": True
    }
    response = requests.post(f"{BASE_URL}/users", json=user_data)
    print(f"CREATE USER: Status {response.status_code}")
    if response.status_code not in [200, 201]:
        print(f"Error: {response.text}")
        return None
    user = response.json()
    user_id = user['userId']
    print(f"Created User ID: {user_id}")
    return user_id

def test_crud_pois(organization_id, user_id):
    print("\n--- Testing Points of Interest CRUD ---")

    # 1. Create
    poi_data = {
        "organization_id": organization_id,
        "created_by_user_id": user_id,
        "poi_name": "Test POI " + str(uuid.uuid4())[:8],
        "poi_type": "RESTAURANT",
        "poi_category": "FOOD_DRINK",
        "poi_description": "A delicious test restaurant",
        "latitude": 3.8480,
        "longitude": 11.5021,
        "address_city": "Yaoundé",
        "address_country": "Cameroon",
        "is_active": True
    }
    response = requests.post(f"{BASE_URL}/pois", json=poi_data)
    print(f"CREATE POI: Status {response.status_code}")
    if response.status_code not in [200, 201]:
        print(f"Error: {response.text}")
        return None

    poi = response.json()
    poi_id = poi['poi_id']
    print(f"Created POI ID: {poi_id}")
    return poi_id

def test_crud_blogs(user_id, poi_id):
    print("\n--- Testing Blogs CRUD ---")

    # 1. Create
    blog_data = {
        "user_id": user_id,
        "poi_id": poi_id,
        "title": "Test Blog " + str(uuid.uuid4())[:8],
        "description": "Interesting blog description",
        "content": "This is a very long content for the blog post...",
        "cover_image_url": "http://example.com/image.jpg"
    }
    response = requests.post(f"{BASE_URL}/blogs", json=blog_data)
    print(f"CREATE BLOG: Status {response.status_code}")
    if response.status_code not in [200, 201]:
        print(f"Error: {response.text}")
        return None

    blog = response.json()
    blog_id = blog['blog_id']
    print(f"Created Blog ID: {blog_id}")
    return blog_id

def cleanup(user_id, poi_id, org_id, blog_id):
    print("\n--- Cleaning up resources ---")
    if blog_id:
        response = requests.delete(f"{BASE_URL}/blogs/{blog_id}")
        print(f"DELETE BLOG {blog_id}: Status {response.status_code}")
    if poi_id:
        response = requests.delete(f"{BASE_URL}/pois/{poi_id}")
        print(f"DELETE POI {poi_id}: Status {response.status_code}")
    if user_id:
        response = requests.delete(f"{BASE_URL}/users/{user_id}")
        print(f"DELETE USER {user_id}: Status {response.status_code}")
    if org_id:
        response = requests.delete(f"{BASE_URL}/organizations/{org_id}")
        print(f"DELETE ORG {org_id}: Status {response.status_code}")

if __name__ == "__main__":
    try:
        # Simple health check
        requests.get(f"{BASE_URL}/organizations", timeout=2)
    except requests.exceptions.ConnectionError:
        print(f"Error: Could not connect to the backend at {BASE_URL}. Make sure it's running.")
    else:
        org_id = test_crud_organizations()
        if org_id:
            user_id = test_crud_users(org_id)
            if user_id:
                poi_id = test_crud_pois(org_id, user_id)
                if poi_id:
                    blog_id = test_crud_blogs(user_id, poi_id)
                    cleanup(user_id, poi_id, org_id, blog_id)
                else:
                    cleanup(user_id, None, org_id, None)
            else:
                cleanup(None, None, org_id, None)
