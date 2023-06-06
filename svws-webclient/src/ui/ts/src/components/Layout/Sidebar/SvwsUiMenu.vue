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
				<svws-ui-button size="small"
					type="transparent"
					@click="modal.openModal()">
					<i-ri-equalizer-line />
					<span class="text-ellipsis-line-clamp">Ansicht</span>
				</svws-ui-button>
			</div>
			<div class="sidebar--menu--footer-credits flex flex-col items-center">
				<div class="opacity-25 text-sm mb-1 text-center">Powered by SVWS NRW</div>
				<svws-ui-button size="small"
					type="transparent"
					@click="modalInfo.openModal()">
					<span class="text-ellipsis-line-clamp">Info</span>
				</svws-ui-button>
			</div>
		</div>
	</div>
	<svws-ui-modal ref="modal" size="small">
		<template #modalTitle>
			Einstellungen
		</template>
		<template #modalDescription>
			<span>Dieser Bereich ist noch in Entwicklung.</span>
		</template>
		<template #modalContent>
			<div class="flex flex-col gap-3 opacity-25 pointer-events-none">
				<div class="flex flex-wrap gap-3 items-center">
					<span>Theme:</span>
					<svws-ui-radio-group :row="true" class="justify-center">
						<svws-ui-radio-option value="auto" v-model="theme" name="theme" label="System" />
						<svws-ui-radio-option value="light" v-model="theme" name="theme" label="Light">
							<i-ri-sun-line />
						</svws-ui-radio-option>
						<svws-ui-radio-option value="dark" v-model="theme" name="theme" label="Dark">
							<i-ri-moon-line />
						</svws-ui-radio-option>
					</svws-ui-radio-group>
				</div>
				<div class="flex flex-wrap gap-3 items-center">
					<span>Ansicht:</span>
					<svws-ui-radio-group :row="true" class="justify-center">
						<svws-ui-radio-option value="10" v-model="fontSize" name="fontSize" label="Kleiner">
							<i-ri-zoom-out-line />
						</svws-ui-radio-option>
						<svws-ui-radio-option value="12" v-model="fontSize" name="fontSize" label="Normal" />
						<svws-ui-radio-option value="14" v-model="fontSize" name="fontSize" label="Größer">
							<i-ri-zoom-in-line />
						</svws-ui-radio-option>
					</svws-ui-radio-group>
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
					Hinweis: Um eine gute Lesbarkeit zu erzeugen, wird bei SVWS-NRW möglichst auf geschlechtsneutrale Begriffe wie Lehrkräfte, Klassenleitung, Erzieher usw. zurückgegriffen. An Stellen, wo das nicht möglich ist, wird versucht alle Geschlechter gleichermaßen zu berücksichtigen.
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

	const theme = ref<string>('auto');
	const fontSize = ref<string>("12");

	const modal = ref<any>(null);
	const modalInfo = ref<any>(null);

</script>

<style lang="postcss">
.sidebar--menu {
	@apply flex min-h-full flex-1 flex-col w-full;
}

.sidebar--menu--body {
	@apply flex-1;
}

.sidebar--menu--body,
.sidebar--menu--footer {
	@apply flex flex-col;
}

.sidebar--menu--footer {
	@apply pt-16;

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
}
</style>
