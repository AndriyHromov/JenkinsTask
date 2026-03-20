const fs = require('fs')
const puppeteer = require('puppeteer')
const lighthouse = require('lighthouse/lighthouse-core/fraggle-rock/api.js')

// Основна функція для одного запуску
async function captureReport(iteration) {
    console.log(`\n=== STARTING LOOP ${iteration} ===`);

    const browser = await puppeteer.launch({
        headless: true,
        defaultViewport: null,
        args: [
            '--start-maximized',
            '--no-sandbox',
            '--disable-setuid-sandbox'
        ]
    })

    const page = await browser.newPage()
    const baseURL = 'http://wp'

    await page.setViewport({ width: 1920, height: 1080 })
    await page.setDefaultTimeout(20000)

    const flow = await lighthouse.startFlow(page, {
        name: `Desktop flow - Run ${iteration}`,
        configContext: {
            settingsOverrides: {
                formFactor: 'desktop',
                screenEmulation: {
                    mobile: false,
                    width: 1920,
                    height: 1080,
                    deviceScaleFactor: 1,
                    disabled: false
                },
                throttlingMethod: 'simulate',
                onlyCategories: ['performance']
            }
        }
    })

    // ================= SELECTORS =================
    const tablesTab       = '.page_item.page-item-13';
    const productSelector = '.product-name.green-box.ic-design';
    const addTableToCart  = '.button.green-box.ic-design';
    const openCartPage    = '.page_item.page-item-31';
    const goToCheckout    = '.to_cart_submit.button.green-box.ic-design';
    const placeOrder      = '.button.green-box.ic-design';

    const inputName       = 'input[name="cart_name"]';
    const inputAddress    = 'input[name="cart_address"]';
    const inputPostal     = 'input[name="cart_postal"]';
    const inputCity       = 'input[name="cart_city"]';
    const inputPhone      = 'input[name="cart_phone"]';
    const inputEmail      = 'input[name="cart_email"]';
    const inputCountry    = 'select[name="cart_country"]';

    try {
        // ================= STEP 1 =================
        console.log(`[${iteration}.1] Main page opened`);
        await flow.navigate(baseURL, { stepName: 'Main page' });

        // ================= STEP 2 =================
        console.log(`[${iteration}.2] Open tables category`);
        await flow.startTimespan({ stepName: 'Category' });

        await page.waitForSelector(tablesTab, {
            visible: true,
            timeout: 30000
        });
        
        await page.click(tablesTab);
        
        await flow.endTimespan();

        // ================= STEP 3 =================
        console.log(`[${iteration}.3] Open product`);
        await flow.startTimespan({ stepName: 'Product' });
        await page.waitForSelector(productSelector);
        const allItems = await page.$$(productSelector);
        const productsOnly = allItems.slice(2);
        const randomIndex = Math.floor(Math.random() * productsOnly.length);
        const targetProduct = productsOnly[randomIndex];
        await targetProduct.evaluate(el => el.scrollIntoView());
        await targetProduct.click();
        await page.waitForNavigation({ waitUntil: 'networkidle0' });
        await flow.endTimespan();

        // ================= STEP 4 =================
        console.log(`[${iteration}.4] Add to cart`);
        await flow.startTimespan({ stepName: 'Add to cart' });
        await page.waitForSelector(addTableToCart, { visible: true, timeout: 10000 });
        await page.click(addTableToCart);
        await page.waitForSelector(openCartPage, { visible: true });
        await flow.endTimespan();

        // ================= STEP 5 =================
        console.log(`[${iteration}.5] Open cart`);
        await flow.startTimespan({ stepName: 'Cart' });
        await page.waitForSelector(openCartPage);
        await page.click(openCartPage);
        await flow.endTimespan();

        // ================= STEP 6 =================
        console.log(`[${iteration}.6] Checkout`);
        await flow.startTimespan({ stepName: 'Checkout' });
        await page.waitForSelector(goToCheckout, { visible: true });
        await page.click(goToCheckout);
        await flow.endTimespan();

        // ================= STEP 7 =================
        console.log(`[${iteration}.7] Fill form & place order`);
        await flow.startTimespan({ stepName: 'Order' });
        await page.waitForSelector(inputName);
        await page.type(inputName, 'Test User');
        await page.type(inputAddress, 'Shevchenka 25');
        await page.type(inputPostal, '27000');
        await page.type(inputCity, 'Kyiv');
        await page.type(inputPhone, '1234567890');
        await page.type(inputEmail, 'test@test.com');
        await page.waitForSelector(inputCountry, { visible: true });
        await page.select(inputCountry, 'UA');

        await page.waitForSelector(placeOrder, { visible: true });
        await page.click(placeOrder);
        
        try {
            await page.waitForSelector('h1.entry-title', { visible: true, timeout: 15000 });
            console.log(`Success: Order placed (Loop ${iteration})!`);
        } catch (e) {
            console.warn(`Warning: Success message not found in Loop ${iteration}.`);
        }
        await flow.endTimespan();

        // ================= REPORT =================
        const reportPath = `${__dirname}/lighthouse-report-${iteration}.html`;
        const report = await flow.generateReport();
        fs.writeFileSync(reportPath, report);
        console.log(`Report saved: ${reportPath}`);

    } catch (error) {
        console.error(`Error in Loop ${iteration}:`, error);
    } finally {
        await browser.close();
    }
}

(async () => {
    await captureReport(1);
    console.log('\n=== TEST COMPLETED ===');
})();