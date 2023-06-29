<template>
	<svws-ui-app-layout :fullwidth-content="true">
		<template #main>
			<div class="error-wrapper">
				<div class="error-container">
					<div class="error-form modal modal--md">
						<div class="modal--content-wrapper">
							<div class="modal--content">
								<div class="error-form-header mb-8 px-8 py-4 mt-6">
									<h1 class="leading-none text-center w-full">
										<span class="font-normal">Es ist ein Fehler aufgetreten</span>
									</h1>
								</div>
								<div v-if="code !== undefined" class="w-full mt-1 px-8">
									HTTP-Fehlercode: {{ code }}
								</div>
								<div v-if="error !== undefined" class="w-full mt-1 px-8">
									<div> Fehler : {{ error.name }} </div>
									<div> Message : {{ error.message }} </div>
									<div> Stack Trace : </div>
									<div class="ml-5"> {{ error.stack }} </div>
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

	import type { ErrorProps } from "./SErrorProps";

	const props = defineProps<ErrorProps>();

</script>


<style lang="postcss">

	.error-wrapper {
		@apply flex h-full flex-col justify-between;
	}

	.error-container {
		@apply bg-cover bg-top rounded-t-2xl h-full flex flex-col justify-center items-center px-4;
		background-image: url("/images/placeholder-background.jpg");
	}

	.modal {
		@apply shadow-black/25 rounded-xl;
	}

	.error-form-header {
		@apply flex flex-row items-start justify-between gap-4 font-bold leading-tight;
		font-size: 2.618rem;
	}
	.error-form .modal--content {
		@apply p-0;
		@apply text-left
	}

</style>
