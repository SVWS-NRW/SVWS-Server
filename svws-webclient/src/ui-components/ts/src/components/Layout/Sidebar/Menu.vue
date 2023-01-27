<template>
	<div
		class="sidebar--menu">
		<div class="sidebar--menu--header">
			<slot name="header" />
		</div>
		<div class="sidebar--menu--body">
			<slot />
		</div>
		<div class="sidebar--menu--footer">
			<slot name="footer" />
			<div class="app--appearance-settings opacity-50 cursor-not-allowed">
				<button type="button"
					class="toggle-theme-button pointer-events-none"
					:title="isDarkTheme ? 'Zum Light Theme wechseln' : 'Zum Dark Theme wechseln (Work in progress!)'"
					@click="toggleDarkTheme">
					<template v-if="isDarkTheme">
						<Icon><i-ri-sun-line /></Icon>
					</template>
					<template v-if="!isDarkTheme">
						<Icon><i-ri-moon-line /></Icon>
					</template>
				</button>
				<button>
					<Icon><i-ri-font-size-2 /></Icon>
				</button>
			</div>
			<div class="sidebar--menu--footer-credits flex flex-col items-center mb-3">
				<div class="opacity-25 text-sm mt-3 mb-1">Powered by SVWS NRW</div>
				<Popover :hover="false">
					<template #trigger>
						<div class="inline-flex justify-center items-center cursor-pointer opacity-50 hover:opacity-100 focus:opacity-100">Info</div>
					</template>
					<template #content>
						<div class="py-1">
							<div class="whitespace-nowrap text-ellipsis-line-clamp opacity-50">
								SVWS-Client
								<span v-if="$slots.version" class="whitespace-nowrap">
									v<slot name="version"/>
								</span>
							</div>
							<div class="flex flex-wrap gap-2 meta-navigation">
								<slot name="metaNavigation" />
							</div>
						</div>
					</template>
				</Popover>
			</div>
		</div>
	</div>
</template>

<script setup lang="ts">
	import { onMounted, ref } from "vue";

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
	}
</script>

<style lang="postcss">
.sidebar--menu {
	@apply flex min-h-full flex-1 flex-col;
	@apply w-12;
}

@media (min-width: 1280px) {
	.sidebar--menu {
		@apply w-20;
	}
}

.sidebar--menu--body {
	@apply flex-1;
}

.sidebar--menu--body,
.sidebar--menu--footer {
	@apply flex flex-col;
}

.sidebar--menu--collapsed .sidebar--menu--body,
.sidebar--menu--collapsed .sidebar--menu--footer {
	@apply px-1;
}

.meta-navigation {
	a {
		@apply underline;

		&:hover,
		&:focus {
			@apply no-underline;
		}
	}
}

.app--appearance-settings {
	@apply mt-4 mb-2 pt-4 border-t-2 border-dark-20 border-opacity-20;
	@apply flex justify-center gap-2;
}
</style>
