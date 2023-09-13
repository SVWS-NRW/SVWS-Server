import type ErrorPage from "./src/ErrorPage";

export {};

declare global {
 namespace PlaywrightTest {
    interface Matchers<R, T> {
        isErrorPageVisible(errorPage : ErrorPage): R;
    }
  }
}

export default global;