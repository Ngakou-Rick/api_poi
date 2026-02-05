import requests
import json
import random
import uuid

# Configuration
BASE_URL = "http://localhost:8080"  # Update if your API runs on a different port
HEADERS = {
    "Content-Type": "application/json"
}

def create_resource(endpoint, data, resource_name):
    """Helper function to create a resource via POST request"""
    url = f"{BASE_URL}{endpoint}"
    try:
        response = requests.post(url, headers=HEADERS, json=data)
        response.raise_for_status()
        created_resource = response.json()
        print(f"✓ Created {resource_name}: {created_resource.get('poi_id') or created_resource.get('userId') or created_resource.get('organizationId') or created_resource.get('blogId') or created_resource.get('podcastId') or 'ID unknown'}")
        return created_resource
    except requests.exceptions.RequestException as e:
        print(f"✗ Failed to create {resource_name}: {e}")
        if hasattr(e, 'response') and e.response is not None:
            print(f"  Response: {e.response.text}")
        return None

def main():
    print("=" * 60)
    print("Starting Test Data Generation")
    print("=" * 60)

    # 1. Create 3 Organizations
    print("\n--- Creating 3 Organizations ---")
    orgs = []
    org_types = ["MERCHANT", "DISTRIBUTOR", "SUPPLIER"]
    org_names = ["Restaurant Le Paradis", "Tech Distributors SA", "Agriculture Supplies Ltd"]
    
    for i in range(3):
        data = {
            "organizationName": org_names[i],
            "orgCode": f"ORG{str(i+1).zfill(3)}",
            "orgType": org_types[i],
            "isActive": True
        }
        resource = create_resource("/api/organizations", data, f"Organization '{org_names[i]}'")
        if resource:
            orgs.append(resource)

    if not orgs:
        print("\n⚠ No organizations created. Aborting.")
        return

    # 2. Create 3 Users
    print("\n--- Creating 3 Users ---")
    users = []
    roles = ["ADMIN", "USER", "USER"]
    user_names = ["admin_user", "poi_creator", "reviewer"]
    
    for i in range(3):
        org_id = orgs[i % len(orgs)]['organizationId']
        data = {
            "organizationId": org_id,
            "username": user_names[i],
            "email": f"{user_names[i]}@test.com",
            "phone": f"+237{str(600000000 + i)}",
            "password": "Password123!",
            "role": roles[i],
            "isActive": True
        }
        resource = create_resource("/api/users", data, f"User '{user_names[i]}'")
        if resource:
            users.append(resource)

    if not users:
        print("\n⚠ No users created. Aborting.")
        return

    # 3. Create 3 POIs (using new CreatePoiDTO format)
    print("\n--- Creating 3 POIs ---")
    pois = []
    poi_data_list = [
        {
            "poi_name": "Chez Wou Restaurant",
            "poi_type": "RESTAURANT",
            "poi_category": "FOOD_DRINK",
            "poi_description": "Restaurant camerounais traditionnel avec spécialités locales",
            "latitude": 3.8480,
            "longitude": 11.5021,
            "address_street_name": "Avenue Kennedy",
            "address_city": "Yaoundé",
            "address_country": "Cameroun",
            "website_url": "https://example.com/chez-wou"
        },
        {
            "poi_name": "Mahima Supermarché",
            "poi_type": "SUPERMARCHE",
            "poi_category": "SHOPPING_RETAIL",
            "poi_description": "Grand supermarché avec produits locaux et importés",
            "latitude": 3.8580,
            "longitude": 11.5121,
            "address_street_name": "Rue de la Réunification",
            "address_city": "Douala",
            "address_country": "Cameroun"
        },
        {
            "poi_name": "Canal Olympia Cinéma",
            "poi_type": "CINEMA",
            "poi_category": "LEISURE_CULTURE",
            "poi_description": "Complexe cinématographique moderne avec derniers films",
            "latitude": 3.8680,
            "longitude": 11.5221,
            "address_street_name": "Boulevard du 20 Mai",
            "address_city": "Yaoundé",
            "address_country": "Cameroun",
            "website_url": "https://example.com/canal-olympia"
        }
    ]
    
    for i, poi_data in enumerate(poi_data_list):
        user_id = users[i % len(users)]['userId']
        org_id = orgs[i % len(orgs)]['organizationId']
        
        # Add required fields
        poi_data["organization_id"] = org_id
        poi_data["created_by_user_id"] = user_id
        
        resource = create_resource("/api/pois", poi_data, f"POI '{poi_data['poi_name']}'")
        if resource:
            pois.append(resource)

    if not pois:
        print("\n⚠ No POIs created. Aborting.")
        return

    # 4. Create 2 Blogs
    print("\n--- Creating 2 Blogs ---")
    blogs = []
    blog_titles = [
        "Les meilleurs restaurants de Yaoundé",
        "Guide d'achat: Produits locaux vs importés"
    ]
    
    for i in range(2):
        user_id = users[i % len(users)]['userId']
        poi_id = pois[i % len(pois)]['poi_id']
        data = {
            "user_id": user_id,
            "poi_id": poi_id,
            "title": blog_titles[i],
            "description": f"Article de blog sur {pois[i % len(pois)]['poi_name']}",
            "content": f"Contenu détaillé de l'article {i+1} avec des informations intéressantes...",
            "cover_image_url": f"https://picsum.photos/800/400?random={i}"
        }
        resource = create_resource("/api/blogs", data, f"Blog '{blog_titles[i]}'")
        if resource:
            blogs.append(resource)

    # 5. Create 2 Podcasts
    print("\n--- Creating 2 Podcasts ---")
    podcasts = []
    podcast_titles = [
        "Interview: Chef Wou parle de sa cuisine",
        "Le commerce au Cameroun: Tendances 2026"
    ]
    
    for i in range(2):
        user_id = users[i % len(users)]['userId']
        poi_id = pois[i % len(pois)]['poi_id']
        data = {
            "user_id": user_id,
            "poi_id": poi_id,
            "title": podcast_titles[i],
            "description": f"Épisode de podcast sur {pois[i % len(pois)]['poi_name']}",
            "audio_file_url": f"https://example.com/audio/podcast_{i+1}.mp3",
            "duration_seconds": 1200 + (i * 300)
        }
        resource = create_resource("/api/podcasts", data, f"Podcast '{podcast_titles[i]}'")
        if resource:
            podcasts.append(resource)

    # 6. Create 3 Reviews
    print("\n--- Creating 3 Reviews ---")
    
    # Review 1: For POI
    if pois and len(users) > 0:
        review_data = {
            "userId": users[0]['userId'],
            "platformType": "Mobile",
            "rating": 5,
            "reviewText": "Excellent restaurant! Nourriture délicieuse et service impeccable.",
            "likes": 0,
            "dislikes": 0
        }
        create_resource(f"/api-review/poi/{pois[0]['poi_id']}", review_data, f"Review for POI '{pois[0]['poi_name']}'")

    # Review 2: For Blog
    if blogs and len(users) > 1:
        blog_id = blogs[0].get('blogId') or blogs[0].get('id')
        if blog_id:
            review_data = {
                "userId": users[1]['userId'],
                "platformType": "Web",
                "rating": 4,
                "reviewText": "Article très informatif, merci pour le partage!",
                "likes": 0,
                "dislikes": 0
            }
            create_resource(f"/api-review/blog/{blog_id}", review_data, f"Review for Blog '{blogs[0].get('title', 'Unknown')}'")

    # Review 3: For Podcast
    if podcasts and len(users) > 2:
        podcast_id = podcasts[0].get('podcastId') or podcasts[0].get('id')
        if podcast_id:
            review_data = {
                "userId": users[2]['userId'],
                "platformType": "Mobile",
                "rating": 5,
                "reviewText": "Discussion très intéressante, j'ai appris beaucoup de choses!",
                "likes": 0,
                "dislikes": 0
            }
            create_resource(f"/api-review/podcast/{podcast_id}", review_data, f"Review for Podcast '{podcasts[0].get('title', 'Unknown')}'")

    print("\n" + "=" * 60)
    print("✓ Test Data Generation Completed Successfully!")
    print("=" * 60)
    print(f"\nSummary:")
    print(f"  - Organizations: {len(orgs)}")
    print(f"  - Users: {len(users)}")
    print(f"  - POIs: {len(pois)} (Status: SUBMITTED)")
    print(f"  - Blogs: {len(blogs)}")
    print(f"  - Podcasts: {len(podcasts)}")
    print(f"  - Reviews: Up to 3 created")
    print(f"\nNote: POIs are created with status 'SUBMITTED' and is_active=false")
    print(f"      Use PATCH /api/pois/{{poi_id}}/approve to approve them.")

if __name__ == "__main__":
    main()
