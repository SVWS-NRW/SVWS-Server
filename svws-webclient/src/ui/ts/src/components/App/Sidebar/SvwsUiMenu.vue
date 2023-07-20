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
			<div class="app--appearance-settings">
				<svws-ui-menu-item subline="" @click="modal.openModal()">
					<template #label>Ansicht</template>
					<template #icon><i-ri-palette-line /></template>
				</svws-ui-menu-item>
			</div>
			<div class="sidebar--menu--footer-credits flex flex-col items-center opacity-25 dark:opacity-50">
				<div class="text-sm mb-2 text-center">Powered by<br>SVWS NRW</div>
				<button role="link" @click="modalInfo.openModal()"
					class="mb-1 hover:opacity-100 underline hover:no-underline text-sm">
					Client Info
				</button>
			</div>
		</div>
	</div>
	<svws-ui-modal ref="modal" size="small">
		<template #modalTitle>
			Einstellungen
		</template>
		<template #modalContent>
			<div class="flex flex-col gap-5">
				<div class="flex flex-col gap-2 text-left">
					<span class="font-bold text-sm">Skalierung</span>
					<svws-ui-radio-group :row="true">
						<svws-ui-radio-option value="small" v-model="fontSize" name="fontSize" label="Kleiner"
							@click="updateFontSize('small')">
							<i-ri-zoom-out-line />
						</svws-ui-radio-option>
						<svws-ui-radio-option value="default" v-model="fontSize" name="fontSize" label="Normal"
							@click="updateFontSize('default')" />
						<svws-ui-radio-option value="large" v-model="fontSize" name="fontSize" label="Größer"
							@click="updateFontSize('large')">
							<i-ri-zoom-in-line />
						</svws-ui-radio-option>
					</svws-ui-radio-group>
				</div>
				<div class="flex flex-col gap-2 text-left">
					<span class="font-bold text-sm">Theme</span>
					<svws-ui-radio-group :row="true">
						<!--<svws-ui-radio-option value="auto" v-model="theme" name="theme" label="System" @click="updateTheme('auto')" />-->
						<svws-ui-radio-option value="light" v-model="theme" name="theme" label="Light"
							@click="updateTheme('light')">
							<i-ri-sun-line />
						</svws-ui-radio-option>
						<svws-ui-radio-option value="dark" v-model="theme" name="theme" label="Dark (In Entwicklung)"
							@click="updateTheme('dark')">
							<i-ri-moon-line />
						</svws-ui-radio-option>
					</svws-ui-radio-group>
					<div v-if="theme === 'dark'" class="mt-2 text-white/50">
						Achtung! Das Dark-Theme befindet sich gerade noch in der Entwicklung und ist noch nicht
						vollständig umgesetzt.
						<span
							class="font-bold text-white">Es kann an einigen Stellen zu Darstellungsproblemen führen.</span>
					</div>
				</div>
			</div>
		</template>
	</svws-ui-modal>
	<svws-ui-modal ref="modalInfo" size="small">
		<template #modalTitle>
			SVWS-Client
		</template>
		<template #modalContent>
			<div class="text-left">
				<div class="mb-5">
					Version
					<slot name="version" />
				</div>
				<p class="text-left opacity-50">
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

	import {ref} from "vue";

	const theme = ref<string>('light');
	const fontSize = ref<string>('default');

	const modal = ref<any>(null);
	const modalInfo = ref<any>(null);

	const updateFontSize = (size: string) => {
		document.documentElement.classList.remove('font-size-small', 'font-size-large');
		if (size !== 'default') {
			document.documentElement.classList.add(`font-size-${size}`);
		}
		localStorage.setItem('fontSize', size);
	};

	const updateTheme = (theme: string) => {
		document.documentElement.classList.remove('light', 'dark');
		if (theme !== 'auto') {
			document.documentElement.classList.add(`${theme}`);
		}
		localStorage.setItem('theme', theme);
	};

	if (localStorage.getItem('theme')) {
		theme.value = localStorage.getItem('theme') as string;
		updateTheme(theme.value);
	}

	if (localStorage.getItem('fontSize')) {
		fontSize.value = localStorage.getItem('fontSize') as string;
		updateFontSize(fontSize.value);
	}
</script>

<style lang="postcss">
.sidebar--menu {
	@apply flex min-h-full flex-1 flex-col w-full;

	@media (orientation: portrait) {
		@apply flex-row min-h-[unset] h-full gap-x-5;
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
	@apply my-5 pt-5;
	@apply flex flex-col gap-1;

	@media (orientation: portrait) {
		@apply my-0 py-0 mx-1;
	}
}
</style>
