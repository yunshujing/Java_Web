import os
import time
import requests
from urllib.parse import urlencode
from requests.adapters import HTTPAdapter
from urllib3.util.retry import Retry

def download_image(url, file_path, max_retries=3):
    # Create directory if it doesn't exist
    os.makedirs(os.path.dirname(file_path), exist_ok=True)
    
    # Add query parameters for image size and quality
    url = f"{url}?w=1920&q=80&fit=crop"
    
    # Set up retry strategy
    retry_strategy = Retry(
        total=max_retries,
        backoff_factor=1,
        status_forcelist=[429, 500, 502, 503, 504],
    )
    
    session = requests.Session()
    adapter = HTTPAdapter(max_retries=retry_strategy)
    session.mount("http://", adapter)
    session.mount("https://", adapter)
    
    for attempt in range(max_retries):
        try:
            response = session.get(url, verify=False, timeout=10)
            response.raise_for_status()
            
            with open(file_path, 'wb') as f:
                f.write(response.content)
            print(f"Successfully downloaded {file_path}")
            return True
            
        except Exception as e:
            print(f"Attempt {attempt + 1}/{max_retries} failed for {file_path}: {str(e)}")
            if attempt < max_retries - 1:
                time.sleep(2 ** attempt)  # Exponential backoff
    
    print(f"Failed to download {file_path} after {max_retries} attempts")
    return False

# List of images to download
images = [
    # 拍摄服务
    ("https://images.unsplash.com/photo-1589829085413-56de8ae18c73", "img/cooperation/corporate-video.jpg"),  # 更新为企业宣传片拍摄场景
    ("https://images.unsplash.com/photo-1596558450255-7c0b7be9d56a", "img/cooperation/douyin.jpg"),  # 更新为年轻人拍摄短视频的场景
    ("https://images.unsplash.com/photo-1533750446969-255bbf191920", "img/cooperation/ad.jpg"),
    ("https://images.unsplash.com/photo-1505678261036-a3fcc5e884ee", "img/cooperation/short-drama.jpg"),
    ("https://images.unsplash.com/photo-1485846234645-a62644f84728", "img/cooperation/film.jpg"),
    
    # 剪辑服务
    ("https://images.unsplash.com/photo-1551650975-87deedd944c3", "img/cooperation/event-edit.jpg"),  # 更新为活动现场剪辑场景
    ("https://images.unsplash.com/photo-1593697821028-7cc59cfd7399", "img/cooperation/douyin-edit.jpg"),  # 更新为短视频编辑工作台
    ("https://images.unsplash.com/photo-1535016120720-40c646be5580", "img/cooperation/film-edit.jpg"),
    ("https://images.unsplash.com/photo-1492619375914-88005aa9e8fb", "img/cooperation/live-edit.jpg"),
    ("https://images.unsplash.com/photo-1563986768494-4dee2763ff3f", "img/cooperation/ad-edit.jpg"),
    
    # 直播服务
    ("https://images.unsplash.com/photo-1598550476439-6847785fcea6", "img/cooperation/douyin-live.jpg"),
    ("https://images.unsplash.com/photo-1571624436279-b272aff752b5", "img/cooperation/kuaishou-live.jpg"),  # 更新为移动直播场景
    ("https://images.unsplash.com/photo-1603739903239-8b6e64c3b185", "img/cooperation/bilibili-live.jpg"),  # 更新为二次元风格直播场景
    ("https://images.unsplash.com/photo-1511512578047-dfb367046420", "img/cooperation/huya-live.jpg"),
    ("https://images.unsplash.com/photo-1561489413-985b06da5bee", "img/cooperation/douyu-live.jpg"),  # 更新为游戏直播场景
]

# Download each image
for url, file_path in images:
    if not download_image(url, file_path):
        print(f"Skipping to next image after failure: {file_path}") 