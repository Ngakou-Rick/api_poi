import requests
import json
import random
import uuid

# Configuration
BASE_URL = "http://localhost:8080" # Update if your API runs on a different port
HEADERS = {
    "Content-Type": "application/json"
}

def create_resource(endpoint, data, resource_name):
    url = f"{BASE_URL}{endpoint}"
    try:
        response = requests.post(url, headers=HEADERS, json=data)
        response.raise_for_status()
        created_resource = response.json()
        print(f"[SUCCESS] Created {resource_name}: {json.dumps(created_resource, indent=2)}")
        return created_resource
    except requests.exceptions.RequestException as e:
        print(f"[ERROR] Failed to create {resource_name}: {e}")
        if e.response is not None:
             print(f"Response: {e.response.text}")
        return None

def main():
    print("--- Starting Test Data Generation ---")

    # 1. Create 3 Organizations
    print("\n--- Creating Organizations ---")
    orgs = []
    org_types = ["MERCHANT", "DISTRIBUTOR", "SUPPLIER"]
    for i in range(3):
        data = {
            "organizationName": f"Org Test {i+1}",
            "orgCode": f"ORG{i+1}",
            "orgType": org_types[i],
            "isActive": True
        }
        resource = create_resource("/api/organizations", data, "Organization")
        if resource:
            orgs.append(resource)

    if not orgs:
        print("No organizations created. Aborting.")
        return

    # 2. Create 3 Users
    print("\n--- Creating Users ---")
    users = []
    roles = ["ADMIN", "USER", "USER"]
    for i in range(3):
        org_id = orgs[i % len(orgs)]['organizationId']
        data = {
            "organizationId": org_id,
            "username": f"user_test_{i+1}",
            "email": f"user{i+1}@test.com",
            "phone": f"123456789{i}",
            "password": "Password123!",
            "role": roles[i],
            "isActive": True
        }
        resource = create_resource("/api/users", data, "User")
        if resource:
            users.append(resource)

    if not users:
        print("No users created. Aborting.")
        return

    # 3. Create 3 POIs
    print("\n--- Creating POIs ---")
    pois = []
    poi_categories = ["FOOD_DRINK", "SHOPPING_RETAIL", "LEISURE_CULTURE"]
    poi_types = ["RESTAURANT", "SUPERMARCHE", "CINEMA"]
    
    for i in range(3):
        user_id = users[i % len(users)]['userId']
        org_id = orgs[i % len(orgs)]['organizationId']
        data = {
            "organization_id": org_id,
            "created_by_user_id": user_id,
            "poi_name": f"POI Test {i+1}",
            "poi_type": poi_types[i],
            "poi_category": poi_categories[i],
            "latitude": 3.8480 + (i * 0.01),
            "longitude": 11.5021 + (i * 0.01),
            "is_active": True,
            "poi_description": "Description for test POI"
        }
        resource = create_resource("/api/pois", data, "POI")
        if resource:
            pois.append(resource)

    if not pois:
        print("No POIs created. Aborting.")
        return

    # 4. Create 2 Blogs
    print("\n--- Creating Blogs ---")
    blogs = []
    for i in range(2):
        user_id = users[i % len(users)]['userId']
        poi_id = pois[i % len(pois)]['poi_id']
        data = {
            "user_id": user_id,
            "poi_id": poi_id,
            "title": f"Blog Post {i+1}",
            "description": "A sample blog post description.",
            "content": "This is the content of the blog post.",
            "cover_image_url": "http://example.com/image.jpg"
        }
        resource = create_resource("/api/blogs", data, "Blog")
        if resource:
            blogs.append(resource)

    # 5. Create 2 Podcasts
    print("\n--- Creating Podcasts ---")
    podcasts = []
    for i in range(2):
        user_id = users[i % len(users)]['userId']
        poi_id = pois[i % len(pois)]['poi_id']
        data = {
            "user_id": user_id,
            "poi_id": poi_id,
            "title": f"Podcast Episode {i+1}",
            "description": "A sample podcast description.",
            "audio_file_url": "http://example.com/audio.mp3",
            "duration_seconds": 300 + (i * 60)
        }
        resource = create_resource("/api/podcasts", data, "Podcast")
        if resource:
            podcasts.append(resource)

    # 6. Create 3 Reviews (1 POI, 1 Blog, 1 Podcast)
    print("\n--- Creating Reviews ---")
    
    review_data_base = {
        "userId": users[0]['userId'],
        "platformType": "Mobile",
        "rating": 5,
        "reviewText": "Excellent service!",
        "likes": 0,
        "dislikes": 0
    }

    # Review for POI
    if pois:
        print(f"Creating review for POI {pois[0]['poi_id']}")
        create_resource(f"/api-review/poi/{pois[0]['poi_id']}", review_data_base, "Review (POI)")

    # Review for Blog
    if blogs:
         # Slightly change data
        review_data_blog = review_data_base.copy()
        review_data_blog['reviewText'] = "Great article!"
        review_data_blog['userId'] = users[1]['userId'] if len(users) > 1 else users[0]['userId']
        print(f"Creating review for Blog {blogs[0]['reviewId'] if 'reviewId' in blogs[0] else 'Unknown'}") # Wait, blog response has ID? check output logging
        # The blog response DTO should have an ID. Usually 'blogId' or 'id'. Let's assume the create returns the DTO.
        # Checking BlogDTO... it usually has an ID.
        # Based on controller, it returns BlogDTO. I'll rely on response.
        blog_id = blogs[0].get('blogId') or blogs[0].get('id')
        if blog_id:
            create_resource(f"/api-review/blog/{blog_id}", review_data_blog, "Review (Blog)")

    # Review for Podcast
    if podcasts:
        review_data_podcast = review_data_base.copy()
        review_data_podcast['reviewText'] = "Interesting discussion."
        review_data_podcast['userId'] = users[2]['userId'] if len(users) > 2 else users[0]['userId']
        podcast_id = podcasts[0].get('podcastId') or podcasts[0].get('id')
        if podcast_id:
             create_resource(f"/api-review/podcast/{podcast_id}", review_data_podcast, "Review (Podcast)")

    print("\n--- Test Data Generation Completed ---")

if __name__ == "__main__":
    main()
