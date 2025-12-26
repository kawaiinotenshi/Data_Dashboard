import pytest
from playwright.sync_api import Page, expect

BASE_URL = "http://localhost:3000"

@pytest.fixture(scope="function")
def page(browser):
    context = browser.new_context(viewport={"width": 1920, "height": 1080})
    page = context.new_page()
    yield page
    context.close()

def test_page_load(page: Page):
    page.goto(BASE_URL)
    expect(page).to_have_title("物流仓储大数据展示")

def test_dashboard_panels_display(page: Page):
    page.goto(BASE_URL)
    page.wait_for_load_state("networkidle")
    
    expect(page.locator("h1")).to_contain_text("物流仓储大数据展示")

def test_dashboard_data_loading(page: Page):
    page.goto(BASE_URL)
    page.wait_for_load_state("networkidle")
    
    canvas_elements = page.locator("canvas")
    expect(canvas_elements).to_have_count(8, timeout=10000)
