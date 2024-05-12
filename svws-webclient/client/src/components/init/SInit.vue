<template>
	<svws-ui-app-layout :fullwidth-content="true">
		<template #main>
			<div class="init-wrapper">
				<div class="init-container">
					<div class="init-form modal modal--md">
						<div class="modal--titlebar">
							<div class="modal--title inline-flex items-center gap-1">
								<span>Initialisierung der Datenbank</span>
							</div>
							<svws-ui-button type="icon" class="invisible" />
						</div>
						<div class="modal--content-wrapper">
							<div class="modal--content overflow-y-auto">
								<div class="flex flex-col">
									<s-init-schulkatalog :list-schulkatalog="listSchulkatalog" :init-schule="initSchule" />
									<s-init-schild2 :migrate-d-b="migrateDB" :set-d-b="setDB" :db="db" />
									<s-init-backup :migrate-d-b="migrateDB" />
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</template>
	</svws-ui-app-layout>
	<s-notifications :backticks="() => true" />
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
