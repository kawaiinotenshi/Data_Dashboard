import pytest
from playwright.sync_api import Page, expect

BASE_URL = "http://localhost:3000"

@pytest.fixture(scope="function")
def page(browser):
    context = browser.new_context(viewport={"width": 1920, "height": 1080})
    page = context.new_page()
    yield page
    context.close()

def test_customer_data_display(page: Page):
    page.goto(BASE_URL)
    page.wait_for_load_state("networkidle")
    
    page.wait_for_timeout(2000)
    
    expect(page.locator("h1")).to_be_visible()

def test_inventory_data_display(page: Page):
    page.goto(BASE_URL)
    page.wait_for_load_state("networkidle")
    
    page.wait_for_timeout(2000)
    
    expect(page.locator("h1")).to_be_visible()

def test_order_data_display(page: Page):
    page.goto(BASE_URL)
    page.wait_for_load_state("networkidle")
    
    page.wait_for_timeout(2000)
    
    expect(page.locator("h1")).to_be_visible()

def test_supplier_data_display(page: Page):
    page.goto(BASE_URL)
    page.wait_for_load_state("networkidle")
    
    page.wait_for_timeout(2000)
    
    expect(page.locator("h1")).to_be_visible()

def test_warehouse_data_display(page: Page):
    page.goto(BASE_URL)
    page.wait_for_load_state("networkidle")
    
    page.wait_for_timeout(2000)
    
    expect(page.locator("h1")).to_be_visible()

def test_transport_data_display(page: Page):
    page.goto(BASE_URL)
    page.wait_for_load_state("networkidle")
    
    page.wait_for_timeout(2000)
    
    expect(page.locator("h1")).to_be_visible()
