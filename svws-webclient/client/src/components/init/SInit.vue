<template>
	<svws-ui-app-layout :fullwidth-content="true">
		<template #main>
			<div class="init-wrapper">
				<div class="init-container">
					<div class="init-form modal">
						<div class="modal--titlebar">
							<div class="modal--title inline-flex items-center gap-1">
								<span>Initialisierung der Datenbank</span>
							</div>
							<svws-ui-button type="icon" class="invisible" />
						</div>
						<div class="modal--content-wrapper">
							<div class="modal--content overflow-y-auto">
								<div v-if="!source" class="mb-6 opacity-50 leading-tight text-left">Wähle eine der folgenden Option aus, um mit der Initialiserung der Datenbank zu beginnen:</div>
								<div class="flex flex-col gap-2">
									<button role="button" class="svws-ui-content-button" :class="{'svws-not-active': source && source !== 'schulkatalog', 'svws-active': source === 'schulkatalog'}" @click="setSource('schulkatalog')">
										<div class="svws-icon"><i-ri-archive-line /></div>
										<div class="flex flex-col">
											<div class="svws-title">Schulkatalog</div>
											<div class="svws-description">Daten werden über die Auswahl der Schulnummer ausgwählt</div>
										</div>
									</button>
									<s-init-schulkatalog v-if="source === 'schulkatalog'" :list-schulkatalog="listSchulkatalog" :init-schule="initSchule" class="mb-20" />
									<button role="button" class="svws-ui-content-button" :class="{'svws-not-active': source && source !== 'schild2', 'svws-active': source === 'schild2'}" @click="setSource('schild2')">
										<div class="svws-icon"><i-ri-database-2-line /></div>
										<div class="flex flex-col">
											<div class="svws-title">Schild 2-Datenbank</div>
											<div class="svws-description">Daten werden über die Auswahl einer existierenden Schild 2-Datenbank importiert.</div>
										</div>
									</button>
									<s-init-schild2 v-if="source === 'schild2'" :migrate-d-b="migrateDB" :set-d-b="setDB" class="mb-20" :db="db" />
									<button role="button" class="svws-ui-content-button" :class="{'svws-not-active': source && source !== 'backup', 'svws-active': source === 'backup'}" @click="setSource('backup')">
										<div class="svws-icon"><i-ri-device-recover-line /></div>
										<div class="flex flex-col">
											<div class="svws-title">Backup</div>
											<div class="svws-description">Daten werden aus einem Backup wiederhergestellt</div>
										</div>
									</button>
									<s-init-backup v-if="source === 'backup'" :migrate-d-b="migrateDB" class="mb-20" />
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</template>
	</svws-ui-app-layout>
</template>

<script setup lang="ts">

	import type { InitProps } from "./SInitProps";

	const props = defineProps<InitProps>()
</script>

<style lang="postcss">
.init-wrapper {
	@apply flex h-full flex-col justify-between;
}

.init-container {
	@apply bg-cover bg-top rounded-2xl h-full flex flex-col justify-center items-center px-4;
	background-image: url("/images/placeholder-background-blurred.jpg");
}

.svws-ui-content-button {
	@apply rounded-lg border-light border p-4 text-balance flex gap-4 text-left;

	&.svws-not-active {
		@apply opacity-50 border-transparent order-1;

		.svws-icon {
			@apply opacity-25;
		}
	}

	&.svws-active {
		@apply border-transparent text-primary bg-primary/10 pointer-events-none;
	}

	&:not(.svws-active):hover,
	&:not(.svws-active):focus-visible {
		@apply outline-none bg-black/10 border-black/10 opacity-100;

		.svws-icon {
			@apply opacity-100;
		}
	}

	&:focus {
		@apply outline-none;
	}

	&:not(.svws-active):focus-visible {
		@apply ring ring-primary/50 ring-offset-1;
	}

	.svws-title {
		@apply font-bold text-headline-md;
	}

	.svws-description {
		@apply opacity-50 leading-tight;
	}

	.svws-icon {
		@apply text-headline-xl w-16 text-center;
	}
}
</style>
