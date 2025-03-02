require('ts-node').register();

export default {
    preset: "jest-preset-angular",
    testEnvironment: "jsdom",
    setupFilesAfterEnv: ["<rootDir>/setup-jest.ts"],
    testMatch: ["**/*.spec.ts"],
    transformIgnorePatterns: ["node_modules/(?!@angular|rxjs)"],
    moduleFileExtensions: ["ts", "js", "html"],
    collectCoverage: false,
    coverageReporters: ["html", "text-summary"],
};