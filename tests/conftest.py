import pytest
from playwright.sync_api import BrowserContext

@pytest.fixture(scope="session")
def browser_context_args(browser_context_args):
    return {
        **browser_context_args,
        "viewport": {"width": 1920, "height": 1080},
        "ignore_https_errors": True,
        "locale": "zh-CN",
    }

@pytest.fixture
def page(context: BrowserContext):
    page = context.new_page()
    page.set_default_timeout(30000)
    yield page
    page.close()
