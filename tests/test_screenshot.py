import pytest
from playwright.sync_api import Page, expect

BASE_URL = "http://localhost:3000"

@pytest.fixture(scope="function")
def page(browser):
    context = browser.new_context(viewport={"width": 1920, "height": 1080})
    page = context.new_page()
    yield page
    context.close()

def test_screenshot_dashboard(page: Page):
    page.goto(BASE_URL)
    page.wait_for_load_state("networkidle")
    page.wait_for_timeout(2000)
    
    page.screenshot(path="screenshots/dashboard.png")
    assert True

def test_screenshot_header(page: Page):
    page.goto(BASE_URL)
    page.wait_for_load_state("networkidle")
    page.wait_for_timeout(2000)
    
    header = page.locator(".header")
    header.screenshot(path="screenshots/header.png")
    assert True

def test_screenshot_content(page: Page):
    page.goto(BASE_URL)
    page.wait_for_load_state("networkidle")
    page.wait_for_timeout(2000)
    
    content = page.locator(".content")
    content.screenshot(path="screenshots/content.png")
    assert True
