<template>
	<footer
		class="status svws-ui-bg-white svws-ui-text-black svws-ui-border-dark-20"
	>
		<s-loading-status v-if="loading"></s-loading-status>
		<div class="status-content">
			<p>Powered by SVWS-NRW v{{version}} - {{hash}}</p>
			<nav class="status-nav">
				<a href="https://www.svws.nrw.de/faq/impressum">Impressum</a>
				<a href="#">Datenschutz</a>
				<a href="#">Hilfe</a>
			</nav>
			<button
				type="button"
				class="toggle-theme-button"
				@click="toggleDarkTheme"
			>
				<template v-if="isDarkTheme">
					<svws-ui-icon><i-ri-sun-line /></svws-ui-icon>
					<span class="hidden 2xl:inline-block">Light Theme</span>
				</template>
				<template v-if="!isDarkTheme">
					<svws-ui-icon><i-ri-moon-line /></svws-ui-icon>
					<span class="hidden 2xl:inline-block">Dark Theme</span>
				</template>
			</button>
		</div>
	</footer>
</template>

<script setup lang="ts">
	import { computed, ComputedRef, onMounted, ref } from "vue";
	import { injectMainApp } from "~/apps/Main";
	import { version, hash } from '../../version';

	const app = injectMainApp();
	const loading: ComputedRef<boolean> = computed(() => app.config.pending);

    const isDarkTheme = ref(false);

    onMounted(() => {
		if (
			localStorage.getItem("darkmode") === "true" &&
			isDarkTheme.value === false
		) {
			toggleDarkTheme();
		}
	});

    function toggleDarkTheme() {
		isDarkTheme.value = !isDarkTheme.value;
		window.localStorage.setItem(
			"darkmode",
			String(isDarkTheme.value)
		);
		if (isDarkTheme.value) {
			document.body.classList.add("theme-dark");
		} else {
			document.body.classList.remove("theme-dark");
		}
	};
</script>

<style>
	footer.status {
		@apply px-4 py-1;
		@apply flex flex-col;
		@apply 2xl:flex-row 2xl:items-center;
		@apply absolute bottom-0;
		@apply w-full;
	}
	.toggle-theme-button {
		@apply flex flex-row items-center;
		@apply space-x-2;
	}
	.toggle-theme-button:focus {
		@apply outline-none ring-2 ring-blue-500 ring-opacity-50;
	}

	.status-nav {
		@apply flex items-center;
		@apply space-x-2;
	}

	.status-progress {
		@apply flex-shrink-0;
		@apply w-64;
	}

	.status-content {
		@apply flex flex-row items-center;
		@apply 2xl:flex-grow 2xl:justify-end;
		@apply text-xs;
		@apply 2xl:text-base;
		@apply space-x-4;
	}
</style>
