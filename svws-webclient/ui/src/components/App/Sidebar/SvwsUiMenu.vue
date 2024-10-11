<template>
	<div
		class="sidebar--menu">
		<div class="sidebar--menu--header" v-if="$slots.header">
			<slot name="header" />
		</div>
		<div v-else class="h-5" />
		<div class="sidebar--menu--body">
			<slot />
		</div>
		<div class="sidebar--menu--footer">
			<slot name="footer" />
			<div class="sidebar--menu--footer-credits flex flex-col items-center text-ui-secondary">
				<div class="text-sm mt-2 mb-2 text-center">Powered by<br>SVWS NRW</div>
				<button role="link" @click="showModalInfo().value = true"
					class="mb-1 underline hover:text-ui-secondary-hover text-sm">
					Client Info
				</button>
			</div>
		</div>
	</div>
	<svws-ui-modal :show="showModalInfo" size="small">
		<template #modalTitle>
			SVWS-Client
		</template>
		<template #modalContent>
			<div class="text-left">
				<div class="mb-5">
					Version
					<slot name="version" />
				</div>
				<p class="text-left text-ui-secondary">
					Hinweis: Um eine gute Lesbarkeit zu erzeugen, wird bei SVWS-NRW möglichst auf geschlechtsneutrale
					Begriffe wie Lehrkräfte, Klassenleitung, Erzieher usw. zurückgegriffen. An Stellen, wo das nicht
					möglich ist, wird versucht alle Geschlechter gleichermaßen zu berücksichtigen.
				</p>
			</div>
		</template>
		<template #modalActions>
			<slot name="metaNavigation" />
		</template>
	</svws-ui-modal>
</template>

<script setup lang="ts">

	import { ref } from "vue";

	const _showModalInfo = ref<boolean>(false);
	const showModalInfo = () => _showModalInfo;

	const props = withDefaults(defineProps<{
		showEinstellungenDefaultApp? : boolean;
	}>(), {
		showEinstellungenDefaultApp: true,
	});


	// function handleBeforePrint() {
	// 	if (themeRef.value === 'dark') {
	// 		document.documentElement.classList.remove('dark');
	// 		document.documentElement.classList.add('light');
	// 	}
	// }

	// function handleAfterPrint() {
	// 	if (themeRef.value === 'dark') {
	// 		document.documentElement.classList.remove('light');
	// 		document.documentElement.classList.add('dark');
	// 	}
	// }

	// onMounted(() => {
	// 	window.addEventListener("beforeprint", handleBeforePrint);
	// 	window.addEventListener("afterprint", handleAfterPrint);
	// });

	// onUnmounted(() => {
	// 	window.removeEventListener("beforeprint", handleBeforePrint);
	// 	window.removeEventListener("afterprint", handleAfterPrint);
	// });
</script>

<style lang="postcss">
.sidebar--menu {
	@apply text-ui;
	@apply flex min-h-full min-w-fit flex-1 flex-col w-full;

	@media (orientation: portrait) {
		@apply flex-row min-h-[unset] gap-x-5;
	}
}

.sidebar--menu--body {
	@apply flex-1;

	@media (orientation: portrait) {
		@apply gap-1;
	}
}

.sidebar--menu--body,
.sidebar--menu--footer {
	@apply flex flex-col;

	@media (orientation: portrait) {
		@apply flex-row;
	}
}

.sidebar--menu--footer {
	@apply pt-16;

	@media (orientation: portrait) {
		@apply pt-0 items-center ml-12;

		.sidebar--menu-item {
			@apply order-1;
		}

		.sidebar--menu--footer-credits {
			@apply order-2 ml-8 min-w-[5rem] mr-3;

			.mb-2 {
				margin-bottom: 0.25rem;
			}
		}
	}

	.text-ellipsis-line-clamp {
		overflow: hidden;
		text-overflow: ellipsis;
		max-width: 100%;
		display: -webkit-box;
		-webkit-box-orient: vertical;
		-webkit-line-clamp: 1;
		line-clamp: 1;
		word-break: break-all;
	}

	.button {
		@apply px-0.5 inline-flex gap-0.5 w-full justify-center;

		svg {
			@apply shrink-0;
		}
	}
}

.sidebar--menu--collapsed .sidebar--menu--body,
.sidebar--menu--collapsed .sidebar--menu--footer {
	@apply px-1;
}

.app--appearance-settings {
	@apply mb-3 mt-1;
	@apply flex flex-col gap-1;

	@media (orientation: portrait) {
		@apply my-0 py-0 mx-1;
	}
}
</style>
